package be.shad.tl.service.parser;

import static be.shad.tl.service.parser.ParserUtil.NAME;
import static be.shad.tl.service.parser.ParserUtil.TASK_ID;
import static be.shad.tl.service.parser.ParserUtil.param;

import java.util.Map;

import be.shad.tl.service.TimeLogger;

public class RenameTaskParser implements Parser {
    public StringParam[] format(String taskId, String name) {
        return new StringParam[] {
                param(TASK_ID, taskId),
                param(NAME, name)};
    }


    public void apply(TimeLogger timeLogger, int revision, Map<String, String> arguments) {
        if (revision == 1) {
            timeLogger.setTaskName(arguments.get(TASK_ID), arguments.get(NAME));
        } else {
            throw new UnsupportedOperationException("Revision " + revision + " is not supported.");
        }
    }
}
