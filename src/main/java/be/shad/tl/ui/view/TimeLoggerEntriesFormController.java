package be.shad.tl.ui.view;

import static be.shad.tl.ui.control.TextFieldCellFactory.forTableColumn;
import static be.shad.tl.util.TimeLoggerUtils.isNotEqual;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.converter.DurationConverter;
import be.shad.tl.ui.converter.EntryOverviewDateConverter;
import be.shad.tl.ui.model.TimeLoggerOverviewEntry;

public class TimeLoggerEntriesFormController {
    private TimeLoggerData timeLoggerData;
    private TimeLogger timeLogger;

    @FXML private TableView<TimeLoggerOverviewEntry> entriesTable;
    @FXML private TableColumn<TimeLoggerOverviewEntry, String> taskNameColumn;
    @FXML private TableColumn<TimeLoggerOverviewEntry, Date> startDateColumn;
    @FXML private TableColumn<TimeLoggerOverviewEntry, Date> endDateColumn;
    @FXML private TableColumn<TimeLoggerOverviewEntry, Long> durationColumn;

    private ObservableList<TimeLoggerOverviewEntry> entries = observableArrayList();

    public void setTimeLogger(TimeLogger timeLogger) {
        this.timeLogger = timeLogger;
    }

    public void setTimeLoggerData(TimeLoggerData timeLoggerData) {
        this.timeLoggerData = timeLoggerData;
    }

    public void refresh() {
        List<TimeLoggerOverviewEntry> rows = new ArrayList<>();
        Collection<TimeLoggerTask> tasks = timeLoggerData.getTasks();
        for(TimeLoggerTask task: tasks) {
            for(TimeLoggerEntry entry: timeLoggerData.getTaskEntries(task.getId())) {
                TimeLoggerOverviewEntry row = new TimeLoggerOverviewEntry();
                row.setTaskId(task.getId());
                row.setTaskName(task.getName());
                row.setEntryId(entry.getId());
                row.setStartDate(entry.getStartDate());
                row.setEndDate(entry.getEndDate());
                if (entry.getEndDate() != null) {
                    row.setDuration(entry.getEndDate().getTime() - entry.getStartDate().getTime());
                }
                rows.add(row);
            }
        }
        rows.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        entries.setAll(rows);
        entriesTable.setEditable(true);
        entriesTable.setItems(entries);
    }

    @FXML
    private void initialize() {
        taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        startDateColumn.setCellFactory(forTableColumn(new EntryOverviewDateConverter()));
        endDateColumn.setCellFactory(forTableColumn(new EntryOverviewDateConverter()));
        durationColumn.setCellFactory(forTableColumn(new DurationConverter()));
    }

    @FXML
    private void handleStartDateEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getStartDate(), event.getNewValue())) {
            timeLogger.setEntryStartDate(event.getRowValue().getEntryId(), event.getNewValue());
            refresh();
        }
    }

    @FXML
    private void handleEndDateEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getEndDate(), event.getNewValue())) {
            timeLogger.setEntryEndDate(event.getRowValue().getEntryId(), event.getNewValue());
            refresh();
        }
    }

    @FXML
    private void handleDurationEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Number> event) {
        if (event.getNewValue() != null) {
            long startTime = event.getRowValue().getStartDate().getTime();
            Date endDate = new Date(startTime + event.getNewValue().longValue());
            if (isNotEqual(event.getRowValue().getEndDate(), endDate)) {
                timeLogger.setEntryEndDate(event.getRowValue().getEntryId(), endDate);
                refresh();
            }
        }
    }

    @FXML
    private void handleSnapToPervious() {
        ObservableList<Integer> selectedIndices = entriesTable.
                selectionModelProperty().get().getSelectedIndices();
        for(Integer selectedIndex: selectedIndices) {
            if (selectedIndex > 0) {
                TimeLoggerOverviewEntry previous = entries.get(selectedIndex-1);
                TimeLoggerOverviewEntry current = entries.get(selectedIndex);
                timeLogger.setEntryStartDate(current.getEntryId(), previous.getEndDate());
            }
        }
        refresh();
    }

    @FXML
    private void handleSnapToNext() {
        ObservableList<Integer> selectedIndices = entriesTable.
                selectionModelProperty().get().getSelectedIndices();
        for(Integer selectedIndex: selectedIndices) {
            if (selectedIndex < entries.size()-1) {
                TimeLoggerOverviewEntry current = entries.get(selectedIndex);
                TimeLoggerOverviewEntry next = entries.get(selectedIndex+1);
                timeLogger.setEntryEndDate(current.getEntryId(), next.getStartDate());
            }
        }
        refresh();
    }

    @FXML
    private void handleSnapTo5m() {
        ObservableList<TimeLoggerOverviewEntry> selectedItems = entriesTable.
                selectionModelProperty().get().getSelectedItems();
        for(TimeLoggerOverviewEntry selectedItem: selectedItems) {
            long startTime = selectedItem.getStartDate().getTime();
            long endTime = selectedItem.getEndDate().getTime();
        }
        refresh();
    }

    @FXML
    private void showAddBefore() {

    }

    @FXML
    private void showAddAfter() {

    }
}
