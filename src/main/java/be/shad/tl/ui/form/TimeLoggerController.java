package be.shad.tl.ui.form;

import static be.shad.tl.util.TimeLoggerUtils.isNotEqual;

import java.util.Date;

import be.shad.tl.service.TimeLogger;
import be.shad.tl.service.TimeLoggerData;
import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTask;
import be.shad.tl.ui.model.TimeLoggerViewEntry;
import be.shad.tl.ui.model.TimeLoggerViewTask;
import be.shad.tl.ui.model.event.EntryChangedEvent;
import be.shad.tl.ui.model.event.Event;
import be.shad.tl.ui.model.event.TaskCreatedEvent;
import be.shad.tl.ui.model.event.TaskModifiedEvent;
import be.shad.tl.ui.model.event.TaskSelectedEvent;
import be.shad.tl.ui.model.event.TaskStartedEvent;
import be.shad.tl.ui.model.event.TaskStoppedEvent;

public class TimeLoggerController {
    private final TimeLoggerData timeLoggerData;
    private final TimeLoggerModel model;
    private final TimeLogger timeLogger;

    public TimeLoggerController(
            TimeLoggerData timeLoggerData,
            TimeLogger timeLogger,
            TimeLoggerModel model) {
        this.timeLoggerData = timeLoggerData;
        this.timeLogger = timeLogger;
        this.model = model;
    }

    private void dispatchEvent(Event event) {
        model.getEventBus().post(event);
    }

    public void selectTask(TimeLoggerViewTask task) {
        dispatchEvent(new TaskSelectedEvent(task == null ? null: task.getId()));
    }

    public void changeActivateTask(String taskId) {
        TimeLoggerTask activeTask = timeLoggerData.getActiveTask();
        if (activeTask != null) {
            timeLogger.stopTask(activeTask.getId());
            dispatchEvent(new TaskStoppedEvent(activeTask.getId()));
            if (taskId.equals(activeTask.getId())) {
                // only stop the task
                return;
            }
        }
        timeLogger.startTask(taskId, new Date());
        dispatchEvent(new TaskStartedEvent(taskId));
    }

    public void updateTaskName(String taskId, String name) {
        TimeLoggerTask task = model.getTimeLoggerData().getTask(taskId);
        if (task != null && isNotEqual(task.getName(), name)) {
            timeLogger.setTaskName(task.getId(), name);
            dispatchEvent(new TaskModifiedEvent(task.getId()));
        }
    }

    public void updateTaskDescription(String taskId, String description) {
        TimeLoggerTask task = model.getTimeLoggerData().getTask(taskId);
        if (task != null && isNotEqual(task.getDescription(), description)) {
            timeLogger.setTaskDescription(task.getId(), description);
            dispatchEvent(new TaskModifiedEvent(task.getId()));
        }
    }

    public void snapToPrevious(TimeLoggerViewEntry current, TimeLoggerViewEntry previous) {
        timeLogger.setEntryStartDate(current.getEntryId(), previous.getEndDate());
        dispatchEvent(new EntryChangedEvent(current.getEntryId()));
    }

    public void snapToNext(TimeLoggerViewEntry current, TimeLoggerViewEntry next) {
        timeLogger.setEntryEndDate(current.getEntryId(), next.getStartDate());
        dispatchEvent(new EntryChangedEvent(current.getEntryId()));
    }

    public void setEntryStartDate(String entryId, Date startDate) {
        timeLogger.setEntryStartDate(entryId, startDate);
        dispatchEvent(new EntryChangedEvent(entryId));
    }

    public void setEntryEndDate(String entryId, Date endDate) {
        TimeLoggerEntry taskEntry = timeLoggerData.getTaskEntry(entryId);
        if (isNotEqual(taskEntry.getEndDate(), endDate)) {
            if (endDate == null) {
                TimeLoggerTask activeTask = timeLoggerData.getActiveTask();
                if (activeTask != null) {
                    timeLogger.stopTask(activeTask.getId());
                    dispatchEvent(new TaskStoppedEvent(activeTask.getId()));
                }
            }
            timeLogger.setEntryEndDate(entryId, endDate);
            dispatchEvent(new EntryChangedEvent(entryId));
        }
    }

    public void setEntryRemark(String entryId, String remark) {
        TimeLoggerEntry taskEntry = timeLoggerData.getTaskEntry(entryId);
        if (isNotEqual(taskEntry.getRemark(), remark)) {
            timeLogger.setEntryRemark(entryId, remark);
            dispatchEvent(new EntryChangedEvent(entryId));
        }
    }

    public void setDuration(String entryId, Number duration) {
        TimeLoggerEntry taskEntry = timeLoggerData.getTaskEntry(entryId);
        if (duration != null) {
            long startTime = taskEntry.getStartDate().getTime();
            Date endDate = new Date(startTime + duration.longValue());
            if (isNotEqual(taskEntry.getEndDate(), endDate)) {
                timeLogger.setEntryEndDate(entryId, endDate);
                dispatchEvent(new EntryChangedEvent(entryId));
            }
        } else {
            setEntryEndDate(entryId, null);
        }
    }

    public void createTask(String name) {
        String taskId = timeLogger.createTask(name);
        dispatchEvent(new TaskCreatedEvent(taskId));
    }

    public void removeTag(String taskId, String tag) {
        timeLogger.removeTagFromTask(taskId, tag);
        dispatchEvent(new TaskModifiedEvent(taskId));
    }

    public void addTag(String taskId, String tag) {
        timeLogger.addTagToTask(taskId, tag);
        dispatchEvent(new TaskModifiedEvent(taskId));
    }

    public String getEntriesAsCsvString() {
        return timeLogger.getEntriesAsCsvString();
    }

    public void removeEntry(String entryId) {
        timeLogger.removeEntry(entryId);
        dispatchEvent(new EntryChangedEvent(entryId));
    }

    public void changeTask(String entryId, String taskId) {
        TimeLoggerEntry taskEntry = timeLoggerData.getTaskEntry(entryId);
        timeLogger.changeTask(entryId, taskId);
        if (taskEntry.getEndDate() != null) {
            dispatchEvent(new TaskStoppedEvent(taskId));
        } else {
            dispatchEvent(new TaskStartedEvent(taskId));
        }
    }
}
