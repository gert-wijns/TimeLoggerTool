package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.CREATE_TASK;
import static be.shad.tl.service.loader.LoaderConstants.TASK_ID;
import be.shad.tl.service.TimeLogger;

public class CreateTaskLoader implements Loader {
    public Parameters unapply(String taskId) {
        return new Parameters(getId(), 1).
                put(TASK_ID, taskId);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.createTask(params.getString(TASK_ID), null);
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return CREATE_TASK;
    }
}
