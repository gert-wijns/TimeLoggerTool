package be.shad.tl.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;


public class TimeLoggerImpl implements TimeLogger {
    private final TimeLoggerPersistence persistence;
    private final TimeLoggerData data;

    public TimeLoggerImpl(TimeLoggerPersistence persistence, TimeLoggerData data) {
        this.persistence = persistence;
        this.data = data;
    }

    @Override
    public String startTask(String taskId, Date startDate) {
        String entryId = UUID.randomUUID().toString();
        startTask(taskId, entryId, startDate);
        return entryId;
    }

    @Override
    public void stopTask(String taskId) {
        stopTask(taskId, new Date());
    }

    @Override
    public void stopTask(String taskId, Date endDate) {
        Collection<TimeLoggerEntry> taskEntries = data.getTaskEntries(taskId);
        for(TimeLoggerEntry entry: taskEntries) {
            if (entry.getEndDate() == null) {
                stopTaskEntry(entry.getId(), endDate);
                return;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot stop "
                + "task [%s] because it wasn't started.", taskId));
    }

    @Override
    public String createTask(String name) {
        return createTask(UUID.randomUUID().toString(), name);
    }

    @Override
    public void addTagToTask(String taskId, String tagId) {
        TimeLoggerTag taskTag = getOrCreateTag(tagId);
        data.addTag(data.getTask(taskId), taskTag);
        persistence.addTagToTask(taskId, tagId);
    }

    private TimeLoggerTag getOrCreateTag(String tagId) {
        TimeLoggerTag taskTag = data.getTaskTag(tagId);
        if (taskTag == null) {
            createTag(tagId);
            return data.getTaskTag(tagId);
        }
        return taskTag;
    }

    @Override
    public void removeTagFromTask(String taskId, String tagId) {
        TimeLoggerTag taskTag = data.getTaskTag(tagId);
        if (taskTag != null) {
            data.removeTag(data.getTask(taskId), taskTag);
            persistence.removeTagFromTask(taskId, tagId);
        }
    }

    @Override
    public void startTask(String taskId, String entryId, Date startDate) {
        TimeLoggerEntry entry = new TimeLoggerEntry();
        entry.setId(entryId);
        entry.setTaskId(taskId);
        entry.setStartDate(startDate);
        data.saveEntry(entry);
        data.addEntry(data.getTask(taskId), entry);
        persistence.startTask(taskId, entryId, startDate);
    }

    @Override
    public void stopTaskEntry(String entryId, Date endDate) {
        data.getTaskEntry(entryId).setEndDate(endDate);
        persistence.stopTaskEntry(entryId, endDate);
    }

    @Override
    public String createTask(String taskId, String name) {
        TimeLoggerTask task = new TimeLoggerTask();
        task.setId(taskId);
        task.setName(name);
        data.saveTask(task);
        persistence.createTask(taskId, name);
        return taskId;
    }

    @Override
    public void setTaskDescription(String taskId, String description) {
        data.getTask(taskId).setDescription(description);
        persistence.setTaskDescription(taskId, description);
    }

    @Override
    public void createTag(String code) {
        TimeLoggerTag tag = new TimeLoggerTag();
        tag.setCode(code);
        data.saveTag(tag);
    }

    @Override
    public void setTagDescription(String code, String description) {
        getOrCreateTag(code).setDescription(description);
        persistence.setTagDescription(code, description);
    }

    @Override
    public void setTaskName(String taskId, String name) {
        data.getTask(taskId).setName(name);
        persistence.setTaskName(taskId, name);
    }

    @Override
    public void setEntryRemark(String entryId, String remark) {
        data.getTaskEntry(entryId).setRemark(remark);
        persistence.setEntryRemark(entryId, remark);
    }

    @Override
    public void removeEntry(String entryId) {
        data.removeEntry(data.getTaskEntry(entryId));
        persistence.removeEntry(entryId);
    }

    @Override
    public void changeTask(String entryId, String taskId) {
        TimeLoggerEntry taskEntry = data.getTaskEntry(entryId);
        removeEntry(entryId);
        String newEntryId = startTask(taskId, taskEntry.getStartDate());
        if (taskEntry.getEndDate() != null) {
            stopTaskEntry(newEntryId, taskEntry.getEndDate());
        }
        if (taskEntry.getRemark() != null) {
            setEntryRemark(newEntryId, taskEntry.getRemark());
        }
    }

    @Override
    public void setEntryStartDate(String entryId, Date startDate) {
        TimeLoggerEntry taskEntry = data.getTaskEntry(entryId);
        if (taskEntry.getEndDate() != null && taskEntry.getEndDate().before(startDate)) {
            taskEntry.setEndDate(startDate);
            persistence.setEntryEndDate(entryId, startDate);
        }
        taskEntry.setStartDate(startDate);
        persistence.setEntryStartDate(entryId, startDate);
    }

    @Override
    public void setEntryEndDate(String entryId, Date endDate) {
        TimeLoggerEntry taskEntry = data.getTaskEntry(entryId);
        if (endDate != null && taskEntry.getStartDate().after(endDate)) {
            taskEntry.setStartDate(endDate);
            persistence.setEntryStartDate(entryId, endDate);
        }
        data.getTaskEntry(entryId).setEndDate(endDate);
        persistence.setEntryEndDate(entryId, endDate);
    }

    @Override
    public String getEntriesAsCsvString() {
        List<TimeLoggerEntry> entries = new ArrayList<>();
        for(TimeLoggerTask task: data.getTasks()) {
            for(TimeLoggerEntry entry: data.getTaskEntries(task.getId())) {
                entries.add(entry);
            }
        }
        Collections.sort(entries, (a, b) -> a.getStartDate().compareTo(b.getStartDate()));

        DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        StringBuilder exportSB = new StringBuilder();
        exportSB.append("TaskId|EntryId|TaskName|EntryStartDate|EntryEndDate|EntryRemark\n");
        for(TimeLoggerEntry entry: entries) {
            TimeLoggerTask task = data.getTask(entry.getTaskId());
            exportSB.append(task.getId());
            exportSB.append("|").append(entry.getId());
            exportSB.append("|").append(task.getName());
            exportSB.append("|").append(isoFormat.format(entry.getStartDate()));
            exportSB.append("|").append(entry.getEndDate() == null ? "": isoFormat.format(entry.getEndDate()));
            exportSB.append("|").append(entry.getRemark() == null ? "": entry.getRemark());
            exportSB.append("\n");
        }
        return exportSB.toString();
    }
}
