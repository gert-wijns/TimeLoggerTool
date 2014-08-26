package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.END_DATE;
import static be.shad.tl.service.loader.LoaderConstants.ENTRY_ID;
import static be.shad.tl.service.loader.LoaderConstants.SET_ENTRY_END_DATE;

import java.util.Date;

import be.shad.tl.service.TimeLogger;

public class SetEntryEndDateLoader implements Loader {
    public Parameters unapply(String entryId, Date endDate) {
        return new Parameters(getId(), 1).
                put(ENTRY_ID, entryId).
                put(END_DATE, endDate);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setEntryEndDate(params.getString(ENTRY_ID), params.getDate(END_DATE));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_ENTRY_END_DATE;
    }
}
