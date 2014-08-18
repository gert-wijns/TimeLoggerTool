package be.shad.tl.service.parser;

import static be.shad.tl.service.parser.ParserUtil.END_DATE;
import static be.shad.tl.service.parser.ParserUtil.ENTRY_ID;
import static be.shad.tl.service.parser.ParserUtil.param;

import java.util.Date;
import java.util.Map;

import be.shad.tl.service.TimeLogger;

public class StopTaskParser {
    public StringParam[] format(String taskId, Date endDate) {
        return new StringParam[] {
                param(ENTRY_ID, taskId),
                param(END_DATE, endDate)};
    }

    public void apply(TimeLogger timeLogger, int revision, Map<String, String> arguments) {
        if (revision == 1) {
            timeLogger.stopTaskEntry(arguments.get(ENTRY_ID), ParserUtil.toDate(arguments.get(END_DATE)));
        } else {
            throw new UnsupportedOperationException("Revision " + revision + " is not supported.");
        }
    }
}
