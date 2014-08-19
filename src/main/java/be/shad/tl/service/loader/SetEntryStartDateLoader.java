package be.shad.tl.service.loader;

import static be.shad.tl.service.loader.LoaderConstants.ENTRY_ID;
import static be.shad.tl.service.loader.LoaderConstants.SET_ENTRY_START_DATE;
import static be.shad.tl.service.loader.LoaderConstants.START_DATE;

import java.util.Date;

import be.shad.tl.service.TimeLogger;

public class SetEntryStartDateLoader implements Loader {
    public Parameters unapply(String entryId, Date startDate) {
        return new Parameters(getId(), 1).
                put(ENTRY_ID, entryId).
                put(START_DATE, startDate);
    }

    @Override
    public void apply(TimeLogger timeLogger, Parameters params) {
        if (params.getRevision() == 1) {
            timeLogger.setEntryStartDate(params.getString(ENTRY_ID), params.getDate(START_DATE));
        } else {
            throw new UnsupportedOperationException("Revision " + params.getRevision() + " is not supported.");
        }
    }

    @Override
    public String getId() {
        return SET_ENTRY_START_DATE;
    }
}
