package be.shad.tl.ui.converter;


public interface StringConverter<T> {
    /**
    * Converts the value provided into its string form for display
    */
    String toDisplayString(T value);

    /**
    * Converts the value provided into its string form for edit
    */
    String toEditString(T value);

    /**
    * Converts the string provided into an object defined by the specific converter.
    * Format of the string and type of the resulting object is defined by the specific converter.
    * @return an object representation of the string passed in.
    */
    T fromString(String string) throws StringConversionError;
}
