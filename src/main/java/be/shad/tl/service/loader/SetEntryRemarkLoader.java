package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.ENTRY_ID;
import static be.shad.tl.service.loader.LoaderConstants.REMARK;
import static be.shad.tl.service.loader.LoaderConstants.SET_ENTRY_REMARK;
import be.shad.tl.service.TimeLogger;

public class SetEntryRemarkLoader implements Loader {
    public Parameters unapply(String entryId, String remark) {
        return new Parameters(getId(), 1).
                put(ENTRY_ID, entryId).
                put(REMARK, remark);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setEntryRemark(params.getString(ENTRY_ID), params.getString(REMARK));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_ENTRY_REMARK;
    }
}
