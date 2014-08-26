package be.shad.tl.ui.converter;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EntryOverviewDateConverter implements StringConverter<Date> {
    private final DateFormat dateFormat;

    public EntryOverviewDateConverter() {
        dateFormat = new SimpleDateFormat("dd/MM hh:mm");
    }

    @Override
    public String toString(Date object) {
        return object == null ? "": dateFormat.format(object);
    }

    @Override
    public Date fromString(String string) throws StringConversionError {
        try {
            Calendar calendar = getInstance();
            calendar.setTime(dateFormat.parse(string));
            calendar.set(Calendar.YEAR, getInstance().get(YEAR));
            return calendar.getTime();
        } catch (ParseException e) {
            throw new StringConversionError("Couldn't convert value", e);
        }
    }
}
