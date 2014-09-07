package be.shad.tl.ui.converter;

public class DefaultStringConverter implements StringConverter<String> {
    @Override
    public String toDisplayString(String value) {
        return (value != null) ? value : "";
    }

    @Override
    public String toEditString(String value) {
        return toDisplayString(value);
    }

    @Override
    public String fromString(String value) throws StringConversionError {
        return value;
    }
}
