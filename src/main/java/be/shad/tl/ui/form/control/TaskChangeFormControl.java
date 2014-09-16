package be.shad.tl.ui.form.control;

import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.model.TimeLoggerViewEntry;
import be.shad.tl.ui.model.TimeLoggerViewTask;

public class TaskChangeFormControl extends AbstractFormControl {

    @FXML ComboBox<TimeLoggerViewTask> taskComboBox;
    private TimeLoggerViewEntry selectedItem;
    private Stage dialog;

    @Override
    protected void initalizeControl() {
        List<TimeLoggerViewTask> tasks = new LinkedList<>();
        for(TimeLoggerTask task: model.getTimeLoggerData().getTasks()) {
            tasks.add(new TimeLoggerViewTask(task));
        }
        taskComboBox.setItems(FXCollections.observableArrayList(tasks));
        taskComboBox.setConverter(new StringConverter<TimeLoggerViewTask>() {
            @Override
            public String toString(TimeLoggerViewTask object) {
                return object.getName();
            }

            @Override
            public TimeLoggerViewTask fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
    }

    public void setTaskLogEntry(TimeLoggerViewEntry selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setDialog(Stage dialog) {
        this.dialog = dialog;
    }

    @FXML
    public void onAccept() {
        TimeLoggerViewTask selectedTask = taskComboBox.getSelectionModel().getSelectedItem();
        if (selectedTask != null && !selectedTask.getId().equals(selectedItem.getTaskId())) {
            controller.changeTask(selectedItem.getEntryId(), selectedTask.getId());
        }
        dialog.close();
    }
}
