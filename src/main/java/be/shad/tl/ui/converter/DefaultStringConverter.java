package be.shad.tl.ui.converter;

public class DefaultStringConverter implements StringConverter<String> {
    @Override
    public String toString(String value) {
        return (value != null) ? value : "";
    }

    @Override
    public String fromString(String value) throws StringConversionError {
        return value;
    }
}
