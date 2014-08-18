package be.shad.tl.service;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;

public class TimeLoggerImpl implements TimeLogger {
    private final TimeLoggerDao dao;

    public TimeLoggerImpl(TimeLoggerDao dao) {
        this.dao = dao;
    }

    @Override
    public void tagTask(String taskId, String tagId) {
        TimeLoggerTag taskTag = dao.getTaskTag(tagId);
        if (taskTag == null) {
            createTag(tagId);
            tagTask(taskId, tagId);
        } else {
            dao.addTag(dao.getTask(taskId), taskTag);
        }
    }

    @Override
    public void startTask(String taskId) {
        startTask(taskId, new Date());
    }

    @Override
    public void startTask(String taskId, Date startDate) {
        TimeLoggerEntry entry = new TimeLoggerEntry();
        entry.setStartDate(startDate);
        dao.saveEntry(entry);
        dao.addEntry(dao.getTask(taskId), entry);
    }

    @Override
    public void stopTask(String taskId) {
        stopTask(taskId, new Date());
    }

    @Override
    public void stopTask(String taskId, Date endDate) {
        Collection<TimeLoggerEntry> taskEntries = dao.getTaskEntries(taskId);
        for(TimeLoggerEntry entry: taskEntries) {
            if (entry.getEndDate() == null) {
                entry.setEndDate(endDate);
                return;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot stop "
                + "task [%s] because it wasn't started.", taskId));
    }

    @Override
    public String createTask(String name) {
        TimeLoggerTask task = new TimeLoggerTask();
        task.setId(UUID.randomUUID().toString());
        task.setName(name);
        dao.saveTask(task);
        return task.getId();
    }

    @Override
    public void setTaskDescription(String taskId, String description) {
        dao.getTask(taskId).setDescription(description);
    }

    @Override
    public void createTag(String code) {
        TimeLoggerTag tag = new TimeLoggerTag();
        tag.setCode(code);
        dao.saveTag(tag);
    }

    @Override
    public void setTagDescription(String code, String description) {
        dao.getTask(code).setDescription(description);
    }
}
