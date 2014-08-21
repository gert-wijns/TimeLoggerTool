package be.shad.tl.util;

import static be.shad.tl.util.TimeLoggerUtils.isEqual;

import java.util.function.Consumer;

import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import org.controlsfx.control.textfield.CustomTextField;

public class TextFields {
    private static final Duration FADE_DURATION = Duration.millis(350);

    /**
     * Revertable field with unfocus on enter and escape,
     * committing the text to the original property on enter.
     */
    public static void setupDefaultTextField(CustomTextField inputField, StringProperty original) {
        setupRevertableTextField(inputField, original);
        inputField.addEventFilter(KeyEvent.ANY, (event) -> {
            if (event.getCode() == KeyCode.ENTER
                    || event.getCode() == KeyCode.ESCAPE) {
                inputField.getParent().requestFocus();
            }
        });
        inputField.setOnAction((action) -> original.set(inputField.getText()));
    }

    public static void setupRevertableTextField(CustomTextField inputField, StringProperty original) {
        setupButton(inputField, (tf) -> revertValue(tf, original), original, "revertable");
        original.addListener((property, oldValue, newValue) -> revertValue(inputField, original));
    }

    /**
     * Set text and move care to the end of the reverted text.
     */
    private static void revertValue(TextField tf, StringProperty original) {
        tf.setText(original.get());
        tf.positionCaret(tf.getLength());
    }

    private static void setupButton(CustomTextField inputField, Consumer<TextField> consumer,
            StringProperty validationValue, String styleClass) {
        inputField.getStyleClass().add("clearable-field"); //styleClass + "-field"); //$NON-NLS-1$

        Region clearButton = new Region();
        clearButton.getStyleClass().addAll("graphic"); //$NON-NLS-1$
        StackPane clearButtonPane = new StackPane(clearButton);
        clearButtonPane.getStyleClass().addAll("clear-button"); //styleClass + "-button"); //$NON-NLS-1$
        clearButtonPane.setOpacity(0.0);
        clearButtonPane.setCursor(Cursor.DEFAULT);
        clearButtonPane.setOnMouseReleased(e -> consumer.accept(inputField));
        inputField.rightProperty().set(clearButtonPane);

        final FadeTransition fader = new FadeTransition(FADE_DURATION, clearButtonPane);
        fader.setCycleCount(1);

        inputField.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable arg0) {
                boolean isEqual = isEqual(inputField.getText(), validationValue.getValue());
                boolean isButtonVisible = fader.getNode().getOpacity() > 0;

                if (isButtonVisible && isEqual) {
                    setButtonVisible(false);
                } else if (!isButtonVisible && !isEqual) {
                    setButtonVisible(true);
                }
            }

            private void setButtonVisible( boolean visible ) {
                fader.setFromValue(visible? 0.0: 1.0);
                fader.setToValue(visible? 1.0: 0.0);
                fader.play();
            }
        });
    }
}
