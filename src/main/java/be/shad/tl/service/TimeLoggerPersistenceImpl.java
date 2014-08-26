package be.shad.tl.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.shad.tl.service.loader.AddTagToTaskLoader;
import be.shad.tl.service.loader.CreateTaskLoader;
import be.shad.tl.service.loader.Loader;
import be.shad.tl.service.loader.Parameters;
import be.shad.tl.service.loader.RemoveEntryLoader;
import be.shad.tl.service.loader.RemoveTagFomTaskLoader;
import be.shad.tl.service.loader.SetEntryEndDateLoader;
import be.shad.tl.service.loader.SetEntryRemarkLoader;
import be.shad.tl.service.loader.SetEntryStartDateLoader;
import be.shad.tl.service.loader.SetTagDescriptionLoader;
import be.shad.tl.service.loader.SetTaskDescriptionLoader;
import be.shad.tl.service.loader.SetTaskNameLoader;
import be.shad.tl.service.loader.StartTaskLoader;
import be.shad.tl.service.loader.StopTaskEntryLoader;

public class TimeLoggerPersistenceImpl implements TimeLoggerPersistence {
    protected final Logger logger = LogManager.getLogger(getClass());

    private final SetTaskDescriptionLoader setTaskDescriptionLoader;
    private final SetTagDescriptionLoader setTagDescriptionLoader;
    private final SetEntryStartDateLoader setEntryStartDateLoader;
    private final RemoveTagFomTaskLoader removeTagFomTaskLoader;
    private final SetEntryEndDateLoader setEntryEndDateLoader;
    private final SetEntryRemarkLoader setEntryRemarkLoader;
    private final StopTaskEntryLoader stopTaskEntryLoader;
    private final AddTagToTaskLoader addTagToTaskLoader;
    private final SetTaskNameLoader setTaskNameLoader;
    private final RemoveEntryLoader removeEntryLoader;
    private final CreateTaskLoader createTaskLoader;
    private final StartTaskLoader startTaskLoader;

    private final Map<String, Loader> loaders;
    private final File save;
    private boolean replaying;

    public TimeLoggerPersistenceImpl(File save) {
        loaders = new HashMap<>();
        setTaskDescriptionLoader = register(new SetTaskDescriptionLoader());
        setTagDescriptionLoader = register(new SetTagDescriptionLoader());
        setEntryStartDateLoader = register(new SetEntryStartDateLoader());
        removeTagFomTaskLoader = register(new RemoveTagFomTaskLoader());
        setEntryEndDateLoader = register(new SetEntryEndDateLoader());
        setEntryRemarkLoader = register(new SetEntryRemarkLoader());
        stopTaskEntryLoader = register(new StopTaskEntryLoader());
        addTagToTaskLoader = register(new AddTagToTaskLoader());
        setTaskNameLoader = register(new SetTaskNameLoader());
        removeEntryLoader = register(new RemoveEntryLoader());
        createTaskLoader = register(new CreateTaskLoader());
        startTaskLoader = register(new StartTaskLoader());
        this.save = save;
    }

    private <T extends Loader> T register(T loader) {
        loaders.put(loader.getId(), loader);
        return loader;
    }

    public void loadSaveFile(TimeLogger timeLogger) {
        LinkedList<Parameters> items = new LinkedList<>();
        try {
            List<String> lines = FileUtils.readLines(save);
            for(int i=0, n=lines.size(); i < n;) {
                String[] header = lines.get(i++).split("\\s");

                String id = header[0];
                int revision = Integer.parseInt(header[1]);
                int args = Integer.parseInt(header[2]);

                Parameters params = new Parameters(id, revision);
                for(int m=i+args; i < m; i++) {
                    String argument = lines.get(i);
                    int split = argument.indexOf(" ");
                    params.put(argument.substring(0, split), argument.substring(split+1));
                }
                items.add(params);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        replaying = true;
        for(Parameters item: items) {
            loaders.get(item.getId()).apply(timeLogger, item);
        }
        replaying = false;
    }

    private interface ParamsProvider {
        Parameters getParameters();
    }

    private void saveToFile(ParamsProvider provider) {
        if(!replaying) {
            Parameters params = provider.getParameters();
            try {
                int count = 0;
                LinkedList<String> lines = new LinkedList<>();
                for(Entry<String, String> entry: params.getParameters().entrySet()) {
                    if (entry.getValue() != null) {
                        lines.add(entry.getKey() + " " + entry.getValue());
                        count++;
                    }
                }
                String line = params.getId() + " " + params.getRevision() + " " + count;
                logger.trace("Saving: {} with: {}", params.getId(), params.getParameters());
                lines.addFirst(line);
                FileUtils.writeLines(save, lines, null, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void startTask(String taskId, String entryId, Date startDate) {
        saveToFile(() -> startTaskLoader.unapply(taskId, entryId, startDate));
    }

    @Override
    public void stopTaskEntry(String entryId, Date endDate) {
        saveToFile(() -> stopTaskEntryLoader.unapply(entryId, endDate));
    }

    @Override
    public void removeEntry(String entryId) {
        saveToFile(() -> removeEntryLoader.unapply(entryId));
    }

    @Override
    public void createTask(String taskId, String name) {
        saveToFile(() -> createTaskLoader.unapply(taskId));
        saveToFile(() -> setTaskNameLoader.unapply(taskId, name));
    }

    @Override
    public void setTaskName(String taskId, String name) {
        saveToFile(() -> setTaskNameLoader.unapply(taskId, name));
    }

    @Override
    public void setTaskDescription(String taskId, String description) {
        saveToFile(() -> setTaskDescriptionLoader.unapply(taskId, description));
    }

    @Override
    public void setTagDescription(String tagId, String description) {
        saveToFile(() -> setTagDescriptionLoader.unapply(tagId, description));
    }

    @Override
    public void setEntryRemark(String entryId, String remark) {
        saveToFile(() -> setEntryRemarkLoader.unapply(entryId, remark));
    }

    @Override
    public void setEntryStartDate(String entryId, Date startDate) {
        saveToFile(() -> setEntryStartDateLoader.unapply(entryId, startDate));
    }

    @Override
    public void setEntryEndDate(String entryId, Date endDate) {
        saveToFile(() -> setEntryEndDateLoader.unapply(entryId, endDate));
    }

    @Override
    public void addTagToTask(String taskId, String tagId) {
        saveToFile(() -> addTagToTaskLoader.unapply(taskId, tagId));
    }

    @Override
    public void removeTagFromTask(String taskId, String tagId) {
        saveToFile(() -> removeTagFomTaskLoader.unapply(taskId, tagId));
    }
}
