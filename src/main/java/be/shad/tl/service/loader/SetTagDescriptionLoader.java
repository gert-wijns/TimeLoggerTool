package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.DESCRIPTION;
import static be.shad.tl.service.loader.LoaderConstants.SET_TAG_DESCRIPTION;
import static be.shad.tl.service.loader.LoaderConstants.TAG_ID;
import be.shad.tl.service.TimeLogger;

public class SetTagDescriptionLoader implements Loader {
    public Parameters unapply(String tagId, String name) {
        return new Parameters(getId(), 1).
                put(TAG_ID, tagId).
                put(DESCRIPTION, name);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setTagDescription(params.getString(TAG_ID), params.getString(DESCRIPTION));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_TAG_DESCRIPTION;
    }
}
