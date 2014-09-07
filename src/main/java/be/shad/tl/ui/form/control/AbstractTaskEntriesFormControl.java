package be.shad.tl.ui.form.control;

import static be.shad.tl.ui.control.TextFieldCellFactory.forTableColumn;
import static be.shad.tl.util.TimeLoggerUtils.isNotEqual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.converter.DateConverter;
import be.shad.tl.ui.converter.DurationConverter;
import be.shad.tl.ui.model.TimeLoggerOverviewEntry;
import be.shad.tl.ui.model.ui.event.EntryChangedEvent;

import com.google.common.eventbus.Subscribe;


public abstract class AbstractTaskEntriesFormControl extends AbstractFormControl {
    @FXML protected TableView<TimeLoggerOverviewEntry> entriesTable;
    @FXML protected TableColumn<TimeLoggerOverviewEntry, String> taskNameColumn;
    @FXML protected TableColumn<TimeLoggerOverviewEntry, Date> startDateColumn;
    @FXML protected TableColumn<TimeLoggerOverviewEntry, Date> endDateColumn;
    @FXML protected TableColumn<TimeLoggerOverviewEntry, String> remarkColumn;
    @FXML protected TableColumn<TimeLoggerOverviewEntry, Long> durationColumn;

    @Override
    protected void initalizeControl() {
        if (taskNameColumn != null) {
            taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        }
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        remarkColumn.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());
        startDateColumn.setCellFactory(forTableColumn(new DateConverter()));
        endDateColumn.setCellFactory(forTableColumn(new DateConverter()));
        durationColumn.setCellFactory(forTableColumn(new DurationConverter()));
        remarkColumn.setCellFactory(forTableColumn());
        entriesTable.setEditable(true);
    }

    @Override
    protected void refresh() {
        List<TimeLoggerOverviewEntry> rows = getEntries();
        rows.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        entriesTable.getItems().setAll(rows);
        if (rows.size() > 0) {
            entriesTable.scrollTo(rows.size() - 1);
        }
    }

    protected abstract List<TimeLoggerOverviewEntry> getEntries();

    protected List<TimeLoggerOverviewEntry> createRows(TimeLoggerTask task) {
        Collection<TimeLoggerEntry> taskEntries = model.getTimeLoggerData().getTaskEntries(task.getId());
        List<TimeLoggerOverviewEntry> rows = new ArrayList<>(taskEntries.size());
        for(TimeLoggerEntry entry: taskEntries) {
            rows.add(createRow(task, entry));
        }
        return rows;
    }

    protected TimeLoggerOverviewEntry createRow(TimeLoggerTask task, TimeLoggerEntry entry) {
        TimeLoggerOverviewEntry row = new TimeLoggerOverviewEntry();
        row.setTaskId(task.getId());
        row.setTaskName(task.getName());
        row.setEntryId(entry.getId());
        row.setStartDate(entry.getStartDate());
        row.setEndDate(entry.getEndDate());
        if (entry.getEndDate() != null) {
            row.setDuration(entry.getEndDate().getTime() - entry.getStartDate().getTime());
        }
        return row;
    }

    @Subscribe
    public void onEntryChanged(EntryChangedEvent event) {
        refresh();
    }

    @FXML
    protected void handleStartDateEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getStartDate(), event.getNewValue())) {
            controller.setEntryStartDate(event.getRowValue().getEntryId(), event.getNewValue());
        }

    }

    @FXML
    protected void handleEndDateEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getEndDate(), event.getNewValue())) {
            controller.setEntryEndDate(event.getRowValue().getEntryId(), event.getNewValue());
        }
    }

    @FXML
    protected void handleDurationEditCommit(CellEditEvent<TimeLoggerOverviewEntry, Number> event) {
        controller.setDuration(event.getRowValue().getEntryId(), event.getNewValue());
    }

    @FXML
    protected void handleRemarkEditCommit(CellEditEvent<TimeLoggerOverviewEntry, String> event) {
        controller.setEntryRemark(event.getRowValue().getEntryId(), event.getNewValue());
    }
}
