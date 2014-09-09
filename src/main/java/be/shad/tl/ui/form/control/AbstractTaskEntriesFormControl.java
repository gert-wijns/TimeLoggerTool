package be.shad.tl.ui.form.control;

import static be.shad.tl.ui.control.TextFieldCellFactory.forTableColumn;
import static be.shad.tl.ui.converter.DateConverter.STANDARD_DATE_CONVERTER;
import static be.shad.tl.ui.converter.DurationConverter.STANDARD_DURATION_CONVERTER;
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
import be.shad.tl.ui.model.TimeLoggerViewEntry;
import be.shad.tl.ui.model.event.EntryChangedEvent;

import com.google.common.eventbus.Subscribe;


public abstract class AbstractTaskEntriesFormControl extends AbstractFormControl {
    @FXML protected TableView<TimeLoggerViewEntry> entriesTable;
    @FXML protected TableColumn<TimeLoggerViewEntry, String> taskNameColumn;
    @FXML protected TableColumn<TimeLoggerViewEntry, Date> startDateColumn;
    @FXML protected TableColumn<TimeLoggerViewEntry, Date> endDateColumn;
    @FXML protected TableColumn<TimeLoggerViewEntry, String> remarkColumn;
    @FXML protected TableColumn<TimeLoggerViewEntry, Long> durationColumn;

    @Override
    protected void initalizeControl() {
        if (taskNameColumn != null) {
            taskNameColumn.setCellValueFactory(cellData -> cellData.getValue().taskNameProperty());
        }
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        remarkColumn.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());
        startDateColumn.setCellFactory(forTableColumn(STANDARD_DATE_CONVERTER));
        endDateColumn.setCellFactory(forTableColumn(STANDARD_DATE_CONVERTER));
        durationColumn.setCellFactory(forTableColumn(STANDARD_DURATION_CONVERTER));
        remarkColumn.setCellFactory(forTableColumn());
        entriesTable.setEditable(true);
    }

    @Override
    protected void refresh() {
        List<TimeLoggerViewEntry> rows = getEntries();
        rows.sort((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
        entriesTable.getItems().setAll(rows);
        if (rows.size() > 0) {
            entriesTable.scrollTo(rows.size() - 1);
        }
    }

    protected abstract List<TimeLoggerViewEntry> getEntries();

    protected List<TimeLoggerViewEntry> createRows(TimeLoggerTask task) {
        Collection<TimeLoggerEntry> taskEntries = model.getTimeLoggerData().getTaskEntries(task.getId());
        List<TimeLoggerViewEntry> rows = new ArrayList<>(taskEntries.size());
        for(TimeLoggerEntry entry: taskEntries) {
            rows.add(createRow(task, entry));
        }
        return rows;
    }

    protected TimeLoggerViewEntry createRow(TimeLoggerTask task, TimeLoggerEntry entry) {
        TimeLoggerViewEntry row = new TimeLoggerViewEntry();
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
    protected void handleStartDateEditCommit(CellEditEvent<TimeLoggerViewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getStartDate(), event.getNewValue())) {
            controller.setEntryStartDate(event.getRowValue().getEntryId(), event.getNewValue());
        }

    }

    @FXML
    protected void handleEndDateEditCommit(CellEditEvent<TimeLoggerViewEntry, Date> event) {
        if (isNotEqual(event.getRowValue().getEndDate(), event.getNewValue())) {
            controller.setEntryEndDate(event.getRowValue().getEntryId(), event.getNewValue());
        }
    }

    @FXML
    protected void handleDurationEditCommit(CellEditEvent<TimeLoggerViewEntry, Number> event) {
        controller.setDuration(event.getRowValue().getEntryId(), event.getNewValue());
    }

    @FXML
    protected void handleRemarkEditCommit(CellEditEvent<TimeLoggerViewEntry, String> event) {
        controller.setEntryRemark(event.getRowValue().getEntryId(), event.getNewValue());
    }
}
