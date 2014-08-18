package be.shad.tl.service;

import org.pcollections.HashTreePMap;
import org.pcollections.PCollection;
import org.pcollections.PMap;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;

public class TimeLoggerDaoImpl implements TimeLoggerDao {
    private PSequence<TimeLoggerTask> tasks = TreePVector.empty();
    private PSequence<TimeLoggerTag> tags = TreePVector.empty();
    private PSequence<TimeLoggerEntry> entries = TreePVector.empty();
    private PMap<String, PCollection<TimeLoggerTag>> taskTags = HashTreePMap.empty();
    private PMap<String, PCollection<TimeLoggerEntry>> taskEntries = HashTreePMap.empty();

    @Override
    public TimeLoggerTask getTask(String id) {
        for(TimeLoggerTask task: tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public TimeLoggerEntry getTaskEntry(String id) {
        for(TimeLoggerEntry entry: entries) {
            if (entry.getId().equals(id)) {
                return entry;
            }
        }
        return null;
    }

    @Override
    public TimeLoggerTag getTaskTag(String code) {
        for(TimeLoggerTag tag: tags) {
            if (tag.getCode().equals(code)) {
                return tag;
            }
        }
        return null;
    }

    @Override
    public PCollection<TimeLoggerTask> getTasks() {
        return tasks;
    }

    @Override
    public PCollection<TimeLoggerTag> getTaskTags(String taskId) {
        return taskTags.getOrDefault(taskId, TreePVector.empty());
    }

    @Override
    public PCollection<TimeLoggerEntry> getTaskEntries(String taskId) {
        return taskEntries.getOrDefault(taskId, TreePVector.empty());
    }

    @Override
    public void addTag(TimeLoggerTask task, TimeLoggerTag tag) {
        taskTags = taskTags.plus(task.getId(), getTaskTags(task.getId()).plus(tag));
    }

    @Override
    public void addEntry(TimeLoggerTask task, TimeLoggerEntry entry) {
        taskEntries = taskEntries.plus(task.getId(), getTaskEntries(task.getId()).plus(entry));
    }

    @Override
    public TimeLoggerTask saveTask(TimeLoggerTask task) {
        tasks = tasks.plus(task);
        return task;
    }

    @Override
    public TimeLoggerTag saveTag(TimeLoggerTag tag) {
        tags = tags.plus(tag);
        return tag;
    }

    @Override
    public TimeLoggerEntry saveEntry(TimeLoggerEntry entry) {
        entries = entries.plus(entry);
        return entry;
    }
}
