package be.shad.tl.util;

import java.util.Objects;
import java.util.function.Consumer;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import be.shad.tl.ui.converter.DurationConverter;

public class TimeLoggerUtils {

    public static String toTimeString(long duration) {
        return new DurationConverter().toDisplayString(duration);
    }

    public static void initTextFieldListener(TextField textField,
            StringProperty binding, Consumer<String> onChange) {
        binding.addListener((value, oldValue, newValue) -> {
            if ((textField.getText() == null && newValue != null)
                    || !textField.getText().equals(newValue)) {
                textField.setText(newValue);
            }
            onChange.accept(newValue);
        });
    }

    public static <T> boolean isNotEqual(T a, T b) {
        return !isEqual(a, b);
    }

    public static <T> boolean isEqual(T a, T b) {
        T ca = convertEmptyStringToNull(a);
        T cb = convertEmptyStringToNull(b);
        return Objects.equals(ca, cb);
    }

    public static <T> T convertEmptyStringToNull(T a) {
        return (a instanceof String && ((String) a).isEmpty()) ? null: a;
    }

    public static void setAnchor(Node child, Double left, Double right, Double top, Double bottom) {
        AnchorPane.setLeftAnchor(child, left);
        AnchorPane.setTopAnchor(child, top);
        AnchorPane.setRightAnchor(child, right);
        AnchorPane.setBottomAnchor(child, bottom);
    }
}
