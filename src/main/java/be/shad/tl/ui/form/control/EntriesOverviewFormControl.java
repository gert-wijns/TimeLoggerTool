package be.shad.tl.ui.form.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.model.TimeLoggerOverviewEntry;
import be.shad.tl.ui.model.ui.event.TaskStartedEvent;
import be.shad.tl.ui.model.ui.event.TaskStoppedEvent;

import com.google.common.eventbus.Subscribe;

public class EntriesOverviewFormControl extends AbstractTaskEntriesFormControl {

    @Subscribe
    public void onTaskStarted(TaskStartedEvent event) {
        refresh();
    }

    @Subscribe
    public void onTaskStopped(TaskStoppedEvent event) {
        refresh();
    }

    @Override
    protected List<TimeLoggerOverviewEntry> getEntries() {
        List<TimeLoggerOverviewEntry> rows = new ArrayList<>();
        Collection<TimeLoggerTask> tasks = model.getTimeLoggerData().getTasks();
        for(TimeLoggerTask task: tasks) {
            rows.addAll(createRows(task));
        }
        return rows;
    }

    @FXML
    private void handleSnapToPervious() {
        ObservableList<Integer> selectedIndices = entriesTable.
                selectionModelProperty().get().getSelectedIndices();
        ObservableList<TimeLoggerOverviewEntry> entries = entriesTable.getItems();
        for(Integer selectedIndex: selectedIndices) {
            if (selectedIndex > 0) {
                TimeLoggerOverviewEntry previous = entries.get(selectedIndex-1);
                TimeLoggerOverviewEntry current = entries.get(selectedIndex);
                controller.snapToPrevious(current, previous);
            }
        }
    }

    @FXML
    private void handleSnapToNext() {
        ObservableList<Integer> selectedIndices = entriesTable.
                selectionModelProperty().get().getSelectedIndices();
        ObservableList<TimeLoggerOverviewEntry> entries = entriesTable.getItems();
        for(Integer selectedIndex: selectedIndices) {
            if (selectedIndex < entries.size()-1) {
                TimeLoggerOverviewEntry current = entries.get(selectedIndex);
                TimeLoggerOverviewEntry next = entries.get(selectedIndex+1);
                controller.snapToNext(current, next);
            }
        }
    }

    @FXML
    private void handleSnapTo5m() {
    }

    @FXML
    private void showAddBefore() {

    }

    @FXML
    private void showAddAfter() {

    }
}
