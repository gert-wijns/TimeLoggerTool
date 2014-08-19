package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.REMOVE_TAG_FROM_TASK;
import static be.shad.tl.service.loader.LoaderConstants.TAG_ID;
import static be.shad.tl.service.loader.LoaderConstants.TASK_ID;
import be.shad.tl.service.TimeLogger;

public class RemoveTagFomTaskLoader implements Loader {
    public Parameters unapply(String taskId, String tagId) {
        return new Parameters(getId(), 1).
                put(TASK_ID, taskId).
                put(TAG_ID, tagId);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.removeTagFromTask(params.getString(TASK_ID), params.getString(TAG_ID));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return REMOVE_TAG_FROM_TASK;
    }
}
