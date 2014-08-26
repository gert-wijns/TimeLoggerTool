package be.shad.tl.ui.converter;

public class StringConversionError extends Exception {
    private static final long serialVersionUID = -5695267967507041116L;

    public StringConversionError() {
        super();
    }

    public StringConversionError(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public StringConversionError(String message, Throwable cause) {
        super(message, cause);
    }

    public StringConversionError(String message) {
        super(message);
    }

    public StringConversionError(Throwable cause) {
        super(cause);
    }
}
