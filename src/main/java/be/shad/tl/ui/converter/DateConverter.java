package be.shad.tl.ui.converter;

import static be.shad.tl.ui.GuiSettings.settings;
import static java.util.Calendar.getInstance;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.ReadOnlyProperty;

public class DateConverter implements StringConverter<Date> {
    private static final ReadOnlyProperty<DateFormat> EDIT_DATE_FORMAT = settings().getEditDateFormat();
    private static final ReadOnlyProperty<DateFormat> DISPLAY_DATE_FORMAT = settings().getDisplayDateFormat();

    @Override
    public String toDisplayString(Date object) {
        return object == null ? "": DISPLAY_DATE_FORMAT.getValue().format(object);
    }

    @Override
    public String toEditString(Date object) {
        return object == null ? "": EDIT_DATE_FORMAT.getValue().format(object);
    }

    @Override
    public Date fromString(String string) throws StringConversionError {
        try {
            Calendar calendar = getInstance();
            calendar.setTime(EDIT_DATE_FORMAT.getValue().parse(string));
            //calendar.set(Calendar.YEAR, getInstance().get(YEAR));
            return calendar.getTime();
        } catch (ParseException e) {
            throw new StringConversionError("Couldn't convert value", e);
        }
    }
}
