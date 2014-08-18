package be.shad.tl.service.parser;

import static be.shad.tl.service.parser.ParserUtil.NAME;
import static be.shad.tl.service.parser.ParserUtil.START_DATE;
import static be.shad.tl.service.parser.ParserUtil.TASK_ID;
import static be.shad.tl.service.parser.ParserUtil.param;
import static be.shad.tl.service.parser.ParserUtil.toDate;

import java.util.Date;
import java.util.Map;

import be.shad.tl.service.TimeLogger;

public class StartTaskParser {
    public StringParam[] format(String taskId, Date startDate) {
        return new StringParam[] {
                param(TASK_ID, taskId),
                param(START_DATE, startDate)};
    }

    public void apply(TimeLogger timeLogger, int revision, Map<String, String> arguments) {
        if (revision == 1) {
            timeLogger.startTask(arguments.get(TASK_ID), toDate(arguments.get(NAME)));
        } else {
            throw new UnsupportedOperationException("Revision " + revision + " is not supported.");
        }
    }
}
