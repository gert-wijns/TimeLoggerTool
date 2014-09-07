package be.shad.tl.service;

import java.util.Collection;
import java.util.Date;
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
    public void startTask(String taskId, Date startDate) {
        startTask(taskId, UUID.randomUUID().toString(), startDate);
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
    public void setEntryStartDate(String entryId, Date startDate) {
        data.getTaskEntry(entryId).setStartDate(startDate);
        persistence.setEntryStartDate(entryId, startDate);
    }

    @Override
    public void setEntryEndDate(String entryId, Date endDate) {
        data.getTaskEntry(entryId).setEndDate(endDate);
        persistence.setEntryEndDate(entryId, endDate);
    }
}
