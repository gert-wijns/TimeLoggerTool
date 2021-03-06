package be.shad.tl.ui.converter;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DurationConverter implements StringConverter<Long> {
    public static final ObjectProperty<DurationConverter> STANDARD_DURATION_CONVERTER = createStandardDurationConverterProperty();
    private static final String[] TIME_UNIT_LITERALS = {"h", "m", "s"};
    private static final TimeUnit[] TIME_UNITS = {HOURS, MINUTES, SECONDS};

    @Override
    public String toDisplayString(Long object) {
        if (object == null) {
            return "";
        }
        long duration = object;
        if (duration <= 0) {
            return "0s";
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < TIME_UNIT_LITERALS.length; i++) {
            long part = TIME_UNITS[i].convert(duration, TimeUnit.MILLISECONDS);
            if (part > 0) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(part).append(TIME_UNIT_LITERALS[i]);
                duration -= TIME_UNITS[i].toMillis(part);
            }
        }
        return sb.toString();
    }

    private static ObjectProperty<DurationConverter> createStandardDurationConverterProperty() {
        return new SimpleObjectProperty<>(new DurationConverter());
    }

    @Override
    public String toEditString(Long value) {
        return toDisplayString(value);
    }

    @Override
    public Long fromString(String duration) throws StringConversionError {
        try {
            long total = 0;
            String part = duration;
            for(int i=0; i < TIME_UNIT_LITERALS.length; i++) {
                int timeUnitIdx = part.indexOf(TIME_UNIT_LITERALS[i]);
                if (timeUnitIdx > 0) {
                    String substring = part.substring(0, timeUnitIdx).trim();
                    total += TIME_UNITS[i].toMillis(parseInt(substring));
                    part = part.substring(timeUnitIdx+1);
                }
            }
            return total;
        } catch (NumberFormatException nfe) {
            throw new StringConversionError(format("Input [%s] is not following format \"((\\d)+h)? ((\\d)+m)? ((\\d)+s)?\"", duration), nfe);
        }
    }
}
