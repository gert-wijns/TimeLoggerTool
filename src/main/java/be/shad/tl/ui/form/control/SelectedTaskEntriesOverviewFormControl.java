package be.shad.tl.ui.form.control;

import java.util.Collections;
import java.util.List;

import be.shad.tl.ui.model.TimeLoggerOverviewEntry;
import be.shad.tl.ui.model.ui.event.TaskSelectedEvent;
import be.shad.tl.ui.model.ui.event.TaskStartedEvent;
import be.shad.tl.ui.model.ui.event.TaskStoppedEvent;

import com.google.common.eventbus.Subscribe;

public class SelectedTaskEntriesOverviewFormControl extends AbstractTaskEntriesFormControl {
    private String selectedTaskId;

    @Subscribe
    public void onSelectedTaskChange(TaskSelectedEvent event) {
        selectedTaskId = event.getTaskId();
        refresh();
    }

    @Override
    protected List<TimeLoggerOverviewEntry> getEntries() {
        if (selectedTaskId == null) {
            return Collections.emptyList();
        }
        return createRows(model.getTimeLoggerData().getTask(selectedTaskId));
    }

    @Subscribe
    public void onTaskStarted(TaskStartedEvent event) {
        if (event.getTaskId().equals(selectedTaskId)) {
            refresh();
        }
    }

    @Subscribe
    public void onTaskStopped(TaskStoppedEvent event) {
        if (event.getTaskId().equals(selectedTaskId)) {
            refresh();
        }
    }

}
