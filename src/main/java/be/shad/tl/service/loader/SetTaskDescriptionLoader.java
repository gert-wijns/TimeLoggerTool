package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.DESCRIPTION;
import static be.shad.tl.service.loader.LoaderConstants.SET_TASK_DESCRIPTION;
import static be.shad.tl.service.loader.LoaderConstants.TASK_ID;
import be.shad.tl.service.TimeLogger;

public class SetTaskDescriptionLoader implements Loader {
    public Parameters unapply(String taskId, String name) {
        return new Parameters(getId(), 1).
                put(TASK_ID, taskId).
                put(DESCRIPTION, name);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setTaskDescription(params.getString(TASK_ID), params.getString(DESCRIPTION));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_TASK_DESCRIPTION;
    }
}
