package be.shad.tl.ui.view;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import be.shad.tl.service.TimeLoggerDao;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.model.TimeLoggerViewEntry;
import be.shad.tl.ui.model.TimeLoggerViewTag;
import be.shad.tl.ui.model.TimeLoggerViewTask;

public class TimeLoggerFormController {
    @FXML private TableView<TimeLoggerViewTask> taskTable;
    @FXML private TableColumn<TimeLoggerViewTask, Boolean> activeColumn;
    @FXML private TableColumn<TimeLoggerViewTask, String> nameColumn;

    @FXML private TableView<TimeLoggerViewEntry> entriesTable;
    @FXML private TableColumn<TimeLoggerViewEntry, Date> startDateColumn;
    @FXML private TableColumn<TimeLoggerViewEntry, Date> endDateColumn;
    @FXML private HBox tagsBox;

    @FXML private Label taskNameCaptionLabel;
    @FXML private TextField taskDescriptionField;

    private ObservableList<TimeLoggerViewTask> tasks = observableArrayList();
    private ObservableList<TimeLoggerViewTag> tags = observableArrayList();
    private ObservableList<TimeLoggerViewEntry> entries = observableArrayList();

    private TimeLoggerDao timeLoggerDao;

    public void setTimeLoggerDao(TimeLoggerDao timeLoggerDao) {
        this.timeLoggerDao = timeLoggerDao;

        refreshTasks();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        activeColumn.setCellValueFactory(cellData -> cellData.getValue().activeProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));
        taskTable.setItems(tasks);

        entriesTable.setItems(entries);
    }

    private void refreshTasks() {
        Collection<TimeLoggerTask> tasks = timeLoggerDao.getTasks();
        this.tasks.setAll(tasks.stream().
                map(t -> new TimeLoggerViewTask(t)).
                collect(Collectors.toList()));
    }

    private void showTaskDetails(TimeLoggerViewTask task) {
        taskDescriptionField.setText(task.getDescription());
        taskNameCaptionLabel.setText(task.getName());

        Collection<TimeLoggerTag> tags = timeLoggerDao.getTaskTags(task.getId());
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

        Collection<TimeLoggerEntry> entries = timeLoggerDao.getTaskEntries(task.getId());
        this.entries.setAll(entries.stream().
            map(e -> new TimeLoggerViewEntry(e.getId(), e.getStartDate(), e.getEndDate())).
            collect(Collectors.toList()));
    }

    @FXML
    private void handleOnTaskTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            TimeLoggerViewTask selectedTask = taskTable.getSelectionModel().getSelectedItem();
            selectedTask.setActive(!selectedTask.isActive());
            System.out.println(selectedTask);
        }
    }
}
