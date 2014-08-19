package be.shad.tl.ui.view;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.control.TaskListCell;
import be.shad.tl.ui.model.ActiveTaskData;
import be.shad.tl.ui.model.TimeLoggerViewEntry;
import be.shad.tl.ui.model.TimeLoggerViewTag;
import be.shad.tl.ui.model.TimeLoggerViewTask;

public class TimeLoggerFormController {
    @FXML private ListView<TimeLoggerViewTask> taskList;
    @FXML private TableView<TimeLoggerViewEntry> entriesTable;
    @FXML private TableColumn<TimeLoggerViewEntry, Date> startDateColumn;
    @FXML private TableColumn<TimeLoggerViewEntry, Date> endDateColumn;
    @FXML private TableColumn<TimeLoggerViewEntry, String> remarkColumn;
    @FXML private HBox tagsBox;

    @FXML private Label taskNameCaptionLabel;
    @FXML private TextField taskCreationField;
    @FXML private TextField taskNameField;
    @FXML private TextField taskDescriptionField;

    private ObservableList<TimeLoggerViewTask> tasks;
    private ObservableList<TimeLoggerViewTag> tags = observableArrayList();
    private ObservableList<TimeLoggerViewEntry> entries = observableArrayList();
    private ActiveTaskData activeTaskData = null;

    private TimeLoggerData timeLoggerData;
    private TimeLogger timeLogger;

    public void setTimeLogger(TimeLogger timeLogger) {
        this.timeLogger = timeLogger;
    }

    public void setTimeLoggerData(TimeLoggerData timeLoggerData) {
        this.timeLoggerData = timeLoggerData;
        refreshTasks();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tasks = observableArrayList(task -> new Observable[] {task.nameProperty(),
                task.activeProperty(), task.durationProperty()});

        // Initialize the person table with the two columns.
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        remarkColumn.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());
        remarkColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        taskList.setCellFactory(value -> new TaskListCell());
        taskList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));
        taskList.setItems(tasks);

        entriesTable.setEditable(true);
        entriesTable.setItems(entries);

        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (activeTaskData != null) {
                activeTaskData.getTask().durationProperty().set(activeTaskData.getDuration());
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void refreshTasks() {
        Collection<TimeLoggerTask> tasks = timeLoggerData.getTasks();
        Collection<TimeLoggerViewTask> viewTasks = new LinkedList<>();
        for(TimeLoggerTask task: tasks) {
            TimeLoggerViewTask viewTask = new TimeLoggerViewTask(task);
            long duration = 0;
            Date activeTaskStartDate = null;
            Collection<TimeLoggerEntry> entries = timeLoggerData.getTaskEntries(task.getId());
            for (TimeLoggerEntry entry: entries) {
                if (entry.getEndDate() != null) {
                    duration += entry.getEndDate().getTime() - entry.getStartDate().getTime();
                } else {
                    activeTaskStartDate = entry.getStartDate();
                    viewTask.setActive(true);
                }
            }
            viewTask.setDuration(duration);
            if (viewTask.isActive()) {
                activeTaskData = new ActiveTaskData(viewTask, activeTaskStartDate);
            }
            viewTasks.add(viewTask);
        }
        this.tasks.setAll(viewTasks);
    }

    private void showTaskDetails(TimeLoggerViewTask task) {
        if (task == null) {
            taskDescriptionField.setText("Task");
            taskDescriptionField.setText("Description");
            tagsBox.getChildren().clear();
        } else {
            taskNameField.setText(task.getName());
            taskDescriptionField.setText(task.getDescription());
            taskNameCaptionLabel.setText(task.getName());

            Collection<TimeLoggerTag> tags = timeLoggerData.getTaskTags(task.getId());
            this.tags.setAll(tags.stream().
                map(t -> new TimeLoggerViewTag(t.getCode(), t.getDescription())).
                collect(Collectors.toList()));

            List<Node> tagNodes = new LinkedList<>();
            for(TimeLoggerTag tag: tags) {
                Label tagLabel = new Label();
                tagLabel.setText(tag.getCode());
                tagNodes.add(tagLabel);
            }
            tagsBox.getChildren().setAll(tagNodes);

            Collection<TimeLoggerEntry> entries = timeLoggerData.getTaskEntries(task.getId());
            this.entries.setAll(entries.stream().
                map(e -> new TimeLoggerViewEntry(e)).
                collect(Collectors.toList()));
        }
    }

    @FXML
    private void handleOnTaskListClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TimeLoggerViewTask selectedTask = getSelectedTask();
            if (activeTaskData != null) {
                boolean sameTask = activeTaskData.getTask().getId().equals(selectedTask.getId());
                stopActiveTask();
                if (sameTask) {
                    // refresh task details
                    showTaskDetails(selectedTask);
                    return;
                }
            }
            Date startDate = new Date();
            activeTaskData = new ActiveTaskData(selectedTask, startDate);
            selectedTask.setActive(true);
            timeLogger.startTask(selectedTask.getId(), startDate);
            showTaskDetails(selectedTask);
        }
    }

    public void stopActiveTask() {
        if (activeTaskData != null) {
            activeTaskData.getTask().setActive(false);
            timeLogger.stopTask(activeTaskData.getTask().getId());
            activeTaskData = null;
        }
    }

    @FXML
    private void handleOnTaskCreationFieldAction() {
        String taskId = timeLogger.createTask(taskCreationField.getText());
        taskCreationField.clear();
        refreshTasks();
        for(TimeLoggerViewTask task: tasks) {
            if (task.getId().equals(taskId)) {
                taskList.getSelectionModel().select(task);
            }
        }
    }

    @FXML
    private void handleOnTaskNameChange() {
        TimeLoggerViewTask selectedTask = getSelectedTask();
        timeLogger.setTaskName(selectedTask.getId(), taskNameField.getText());
        selectedTask.setName(taskNameField.getText());
        taskNameCaptionLabel.setText(taskNameField.getText());
    }

    @FXML
    private void handleOnTaskDescriptionChange() {
        TimeLoggerViewTask selectedTask = getSelectedTask();
        timeLogger.setTaskDescription(selectedTask.getId(), taskDescriptionField.getText());
        selectedTask.setDescription(taskDescriptionField.getText());
    }

    @FXML
    private void handleRemarkEditCommit(CellEditEvent<TimeLoggerViewEntry, String> event) {
        TimeLoggerViewEntry selectedItem = entriesTable.getSelectionModel().getSelectedItem();
        timeLogger.setEntryRemark(selectedItem.getId(), event.getNewValue());
    }

    private TimeLoggerViewTask getSelectedTask() {
        return taskList.getSelectionModel().getSelectedItem();
    }
}
