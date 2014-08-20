package be.shad.tl.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        
        BooleanProperty armed = new SimpleBooleanProperty(false);
        ChangeListener<Boolean> focusListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (!arg2 && armed.get() && !textField.getText().equals(binding.get())) {
                    armed.set(false);
                    binding.set(textField.getText());
                } else {
                    armed.set(true);
                }
            }
        };
        textField.focusedProperty().addListener(focusListener);
        textField.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    armed.set(false);
                    //textField.setText(binding.get());
                    textField.getParent().requestFocus();
                } else if (event.getCode() == KeyCode.ENTER) {
                    event.consume();
                    binding.set(textField.getText());
                }
            }
        });
    }
}
