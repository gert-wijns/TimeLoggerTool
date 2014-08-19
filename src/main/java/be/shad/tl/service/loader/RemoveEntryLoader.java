package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.ENTRY_ID;
import static be.shad.tl.service.loader.LoaderConstants.REMOVE_ENTRY;
import be.shad.tl.service.TimeLogger;

public class RemoveEntryLoader implements Loader {
    public Parameters unapply(String entryId) {
        return new Parameters(getId(), 1).
                put(ENTRY_ID, entryId);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.removeEntry(params.getString(ENTRY_ID));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return REMOVE_ENTRY;
    }
}
