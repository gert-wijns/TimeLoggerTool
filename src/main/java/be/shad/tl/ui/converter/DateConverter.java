package be.shad.tl.ui.converter;

import static be.shad.tl.ui.GuiSettings.settings;
import static java.util.Calendar.getInstance;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public class DateConverter implements StringConverter<Date> {
    public static final ObjectProperty<DateConverter> STANDARD_DATE_CONVERTER = createStandardDateConverterProperty();

    private DateFormat editDateFormat;
    private DateFormat displayDateFormat;

    public DateConverter(DateFormat editDateFormat, DateFormat displayDateFormat) {
        this.editDateFormat = editDateFormat;
        this.displayDateFormat = displayDateFormat;
    }

    private static ObjectProperty<DateConverter> createStandardDateConverterProperty() {
        SimpleObjectProperty<DateConverter> property = new SimpleObjectProperty<>(createStandardDateConverter());
        ChangeListener<DateFormat> listener = ((obs, o, n) -> property.setValue(createStandardDateConverter()));
        settings().getEditDateFormat().addListener(listener);
        settings().getDisplayDateFormat().addListener(listener);
        return property;
    }

    private static DateConverter createStandardDateConverter() {
        return new DateConverter(
                settings().getEditDateFormat().getValue(),
                settings().getDisplayDateFormat().getValue());
    }

    @Override
    public String toDisplayString(Date object) {
        return object == null ? "": displayDateFormat.format(object);
    }

    @Override
    public String toEditString(Date object) {
        return object == null ? "": editDateFormat.format(object);
    }

    @Override
    public Date fromString(String string) throws StringConversionError {
        try {
            Calendar calendar = getInstance();
            calendar.setTime(editDateFormat.parse(string));
            //calendar.set(Calendar.YEAR, getInstance().get(YEAR));
            return calendar.getTime();
        } catch (ParseException e) {
            throw new StringConversionError("Couldn't convert value", e);
        }
    }
}
