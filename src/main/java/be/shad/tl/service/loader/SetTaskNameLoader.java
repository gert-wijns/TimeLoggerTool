package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.NAME;
import static be.shad.tl.service.loader.LoaderConstants.SET_TASK_NAME;
import static be.shad.tl.service.loader.LoaderConstants.TASK_ID;
import be.shad.tl.service.TimeLogger;

public class SetTaskNameLoader implements Loader {
    public Parameters unapply(String taskId, String name) {
        return new Parameters(getId(), 1).
                put(TASK_ID, taskId).
                put(NAME, name);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setTaskName(params.getString(TASK_ID), params.getString(NAME));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_TASK_NAME;
    }
}
