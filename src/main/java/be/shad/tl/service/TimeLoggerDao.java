package be.shad.tl.service;

import java.util.Collection;

import be.shad.tl.service.model.TimeLoggerEntry;
import be.shad.tl.service.model.TimeLoggerTag;
import be.shad.tl.service.model.TimeLoggerTask;

public interface TimeLoggerDao {

    TimeLoggerTask getTask(String id);

    TimeLoggerTag getTaskTag(String code);

    TimeLoggerEntry getTaskEntry(String id);

    Collection<TimeLoggerTask> getTasks();

    Collection<TimeLoggerTag> getTaskTags(String taskId);

    Collection<TimeLoggerEntry> getTaskEntries(String taskId);

    void addTag(TimeLoggerTask task, TimeLoggerTag tag);

    void addEntry(TimeLoggerTask task, TimeLoggerEntry entry);

    TimeLoggerTask saveTask(TimeLoggerTask task);

    TimeLoggerTag saveTag(TimeLoggerTag tag);

    TimeLoggerEntry saveEntry(TimeLoggerEntry entry);
}
