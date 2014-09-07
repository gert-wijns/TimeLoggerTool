package be.shad.tl.ui.form.control;

import static be.shad.tl.util.TextFields.setupDefaultTextField;
import static be.shad.tl.util.TimeLoggerUtils.isNotEqual;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.controlsfx.control.textfield.CustomTextField;

import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.model.ui.event.TaskModifiedEvent;
import be.shad.tl.ui.model.ui.event.TaskSelectedEvent;

import com.google.common.eventbus.Subscribe;

public class TaskDetailsFormControl extends AbstractFormControl {
    @FXML private Label taskNameCaptionLabel;
    @FXML private CustomTextField taskNameField;
    @FXML private CustomTextField taskDescriptionField;
    @FXML private HBox tagsBox;

    private String taskId;
    private StringProperty taskNameValue = new SimpleStringProperty();
    private StringProperty taskDescriptionValue = new SimpleStringProperty();

    @Override
    protected void initalizeControl() {
        setupDefaultTextField(taskNameField, taskNameValue);
        setupDefaultTextField(taskDescriptionField, taskDescriptionValue);
        taskNameValue.addListener((p, o, n) -> handleOnTaskNameChange(n));
        taskDescriptionValue.addListener((p, o, n) -> handleOnTaskDescriptionChange(n));
    }

    /**
     * Update task details if the modified task is the task being displayed.
     */
    @Subscribe
    public void onTaskModified(TaskModifiedEvent e) {
        if (e.getTaskId().equals(taskId)) {
            // update displayed task details
            updateTaskDetails(taskId);
        }
    }

    /**
     * Update task details to show the new selected task
     */
    @Subscribe
    public void onTaskSelected(TaskSelectedEvent e) {
        if (!e.getTaskId().equals(taskId)) {
            // update displayed task details
            updateTaskDetails(e.getTaskId());
        }
    }

    private void updateTaskDetails(String taskId) {
        this.taskId = taskId;
        if (taskId == null) {
            taskDescriptionField.setText("");
            taskNameField.setText("Task");
            taskNameCaptionLabel.setText("Task");
            taskNameField.setEditable(false);
            taskDescriptionField.setEditable(false);
        } else {
            taskNameField.setEditable(true);
            taskDescriptionField.setEditable(true);
            TimeLoggerTask task = model.getTimeLoggerData().getTask(taskId);
            if (isNotEqual(taskDescriptionValue.get(), task.getDescription())) {
                taskDescriptionValue.set(task.getDescription());
                taskDescriptionField.setText(task.getDescription());
            }
            if (isNotEqual(taskNameValue.get(), task.getName())) {
                taskNameValue.set(task.getName());
                taskNameField.setText(task.getName());
            }
            if (isNotEqual(taskNameCaptionLabel.getText(), task.getName())) {
                taskNameCaptionLabel.setText(task.getName());
            }
        }
    }

    private void handleOnTaskNameChange(String name) {
        if (taskId != null) {
            controller.updateTaskName(taskId, name);
        }
    }

    private void handleOnTaskDescriptionChange(String description) {
        if (taskId != null) {
            controller.updateTaskDescription(taskId, description);
        }
    }
}
