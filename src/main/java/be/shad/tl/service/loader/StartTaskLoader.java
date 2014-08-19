package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.ENTRY_ID;
import static be.shad.tl.service.loader.LoaderConstants.START_DATE;
import static be.shad.tl.service.loader.LoaderConstants.START_TASK;
import static be.shad.tl.service.loader.LoaderConstants.TASK_ID;

import java.util.Date;

import be.shad.tl.service.TimeLogger;

public class StartTaskLoader implements Loader {
    public Parameters unapply(String taskId, String entryId, Date startDate) {
        return new Parameters(getId(), 1).
                put(TASK_ID, taskId).
                put(ENTRY_ID, entryId).
                put(START_DATE, startDate);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.startTask(params.getString(TASK_ID), params.getString(ENTRY_ID), params.getDate(START_DATE));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return START_TASK;
    }
}
