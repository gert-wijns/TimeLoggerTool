package be.shad.tl.util;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TimeLoggerUtils {
    public static String toTimeString(long duration) {
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration - TimeUnit.HOURS.toMillis(hours));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));
        if (hours > 0 ){
            return String.format("%dh %2dm %2ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %2ds", minutes, seconds);
        } else if (seconds > 0) {
            return String.format("%ds", seconds);
        } else {
            return "";
        }
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
