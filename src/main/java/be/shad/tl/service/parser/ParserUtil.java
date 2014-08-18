package be.shad.tl.service.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserUtil {
    public static final String TASK_ID = "task-id";
    public static final String ENTRY_ID = "entry-id";
    public static final String TAG_ID = "tag-id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start-date";
    public static final String END_DATE = "end-date";
    public static final String REMARK = "remark";

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static final StringParam param(String key, Object value) {
        String parsed = null;
        if (value == null) {
            return new StringParam(key, null);
        } else if (value instanceof String) {
            parsed = (String) value;
        } else if (value instanceof Date) {
            parsed = toString((Date) value);
        } else {
            throw new IllegalArgumentException("Can't parse value toString: " + value);
        }
        return new StringParam(key, parsed);
    }

    public static final Date toDate(String value) {
        if (value == null) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Argument couldn't be parsed.", e);
        }
    }

    public static final String toString(Date value) {
        return DATE_FORMAT.format(value);
    }
}
