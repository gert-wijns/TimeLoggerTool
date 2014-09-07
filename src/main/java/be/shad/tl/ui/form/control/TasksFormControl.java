package be.shad.tl.ui.form.control;

import static be.shad.tl.util.TextFields.setupDefaultTextField;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import org.controlsfx.control.textfield.CustomTextField;

import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.control.TaskListCell;
import be.shad.tl.ui.model.TimeLoggerViewTask;
import be.shad.tl.ui.model.event.EntryChangedEvent;
import be.shad.tl.ui.model.event.TaskCreatedEvent;
import be.shad.tl.ui.model.event.TaskModifiedEvent;
import be.shad.tl.ui.model.event.TaskStartedEvent;
import be.shad.tl.ui.model.event.TaskStoppedEvent;

import com.google.common.eventbus.Subscribe;

public class TasksFormControl extends AbstractFormControl {
    public static final String LOCKED_TAG = "locked";

    @FXML private CustomTextField taskCreationField;
    @FXML private ListView<TimeLoggerViewTask> taskList;
    private StringProperty taskCreationValue = new SimpleStringProperty();
    private Map<String, TimeLoggerViewTask> taskMap;
    private TimeLoggerViewTask activeTask;

    @Override
    protected void initalizeControl() {
        taskMap = new HashMap<>();
        taskList.setCellFactory(value -> new TaskListCell());
        taskList.setItems(observableArrayList(task -> new Observable[] {
                task.nameProperty(), task.durationProperty()}));
        taskList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                controller.selectTask(newValue));
        setupDefaultTextField(taskCreationField, taskCreationValue);
        taskCreationValue.addListener((p, o, n) -> handleOnTaskCreation(n));

        final Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1), event -> {
            if (activeTask != null) {
                updateTaskDuration(activeTask);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @Override
    protected void refresh() {
        Collection<TimeLoggerTask> tasks = this.model.getTimeLoggerData().getTasks();
        Collection<TimeLoggerViewTask> items = new ArrayList<>(tasks.size());
        Map<String, TimeLoggerViewTask> taskMap = new HashMap<>();
        for(TimeLoggerTask task: tasks) {
            TimeLoggerViewTask item = new TimeLoggerViewTask(task);
            taskMap.put(item.getId(), item);
            items.add(item);
            updateTaskDetails(item);
            updateTaskDuration(item);
        }
        this.taskList.getItems().setAll(items);
        this.taskMap = taskMap;
    }

    @Subscribe
    public void onTaskCreated(TaskCreatedEvent event) {
        refresh();
        for(TimeLoggerViewTask task: this.taskList.getItems()) {
            if (task.getId().equals(event.getTaskId())) {
                taskList.getSelectionModel().select(task);
                break;
            }
        }
    }

    @Subscribe
    public void onTaskModified(TaskModifiedEvent event) {
        updateTaskDetails(event.getTaskId());
    }

    @Subscribe
    public void onTaskStarted(TaskStartedEvent event) {
        updateTaskDuration(event.getTaskId());
    }

    @Subscribe
    public void onTaskStopped(TaskStoppedEvent event) {
        updateTaskDuration(event.getTaskId());
    }

    @Subscribe
    public void onEntryChanged(EntryChangedEvent event) {
        TimeLoggerEntry taskEntry = model.getTimeLoggerData().
                getTaskEntry(event.getEntryId());
        updateTaskDuration(taskEntry.getTaskId());
    }

    private void updateTaskDetails(String taskId) {
        updateTaskDetails(taskMap.get(taskId));
    }

    private void updateTaskDuration(String taskId) {
        updateTaskDuration(taskMap.get(taskId));
    }

    /**
     * Updates the total duration of the task.
     */
    private void updateTaskDetails(TimeLoggerViewTask item) {
        TimeLoggerTask task = model.getTimeLoggerData().
                getTask(item.getId());
        item.setDescription(task.getDescription());
        item.setName(task.getName());
        item.setLocked(isLocked(item.getId()));
    }

    private boolean isLocked(String taskId) {
        Collection<TimeLoggerTag> tags = model.getTimeLoggerData().getTaskTags(taskId);
        for(TimeLoggerTag tag: tags) {
            if (LOCKED_TAG.equals(tag.getCode())) {
                return true;
            }
        }
        return false;
    }

    private void updateTaskDuration(TimeLoggerViewTask item) {
        long duration = 0;
        boolean active = false;
        for(TimeLoggerEntry entry: model.getTimeLoggerData().getTaskEntries(item.getId())) {
            if (entry.getEndDate() == null) {
                active = true;
                activeTask = item;
                duration += (System.currentTimeMillis() - entry.getStartDate().getTime());
            } else {
                duration += (entry.getEndDate().getTime() - entry.getStartDate().getTime());
            }
        }
        item.setActive(active);
        item.setDuration(duration);
    }

    @FXML
    private void handleOnTaskListClick(MouseEvent event) {
        TimeLoggerViewTask selectedItem = taskList.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
            if (!selectedItem.isLocked()) {
                controller.changeActivateTask(selectedItem.getId());
            }
        } else if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {
            if (!selectedItem.isActive()) {
                showPopupMenu(event, selectedItem);
            }
        }
    }

    private void showPopupMenu(MouseEvent event, TimeLoggerViewTask selectedItem) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem lockMenuItem = new MenuItem(selectedItem.isLocked() ? "Unlock": "Lock");
        lockMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (selectedItem.isLocked()) {
                    controller.removeTag(selectedItem.getId(), LOCKED_TAG);
                } else {
                    controller.addTag(selectedItem.getId(), LOCKED_TAG);
                }
                contextMenu.hide();
            }
        });
        contextMenu.getItems().add(lockMenuItem);
        contextMenu.show(event.getPickResult().getIntersectedNode(), event.getScreenX(), event.getScreenY());
    }

    private void handleOnTaskCreation(String name) {
        if (name != null) {
            taskCreationValue.set(null);
            controller.createTask(name);
        }
    }

}
