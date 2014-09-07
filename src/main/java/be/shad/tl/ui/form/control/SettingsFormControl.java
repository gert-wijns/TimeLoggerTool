package be.shad.tl.ui.form.control;

import static be.shad.tl.util.TextFields.setupDefaultTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

import org.controlsfx.control.textfield.CustomTextField;

import be.shad.tl.ui.GuiSettings;

public class SettingsFormControl extends AbstractFormControl {
    private @FXML CustomTextField editDateFormatField;
    private @FXML CustomTextField displayDateFormatField;

    private StringProperty editDateFormatValue = new SimpleStringProperty();
    private StringProperty displayDateFormatValue = new SimpleStringProperty();

    @Override
    protected void initalizeControl() {
        setupDefaultTextField(editDateFormatField, editDateFormatValue);
        editDateFormatValue.set(GuiSettings.settings().getEditDateFormatString().get());
        editDateFormatValue.addListener((p, o, n) -> handleOnEditDateFormatChange(n));

        setupDefaultTextField(displayDateFormatField, displayDateFormatValue);
        displayDateFormatValue.set(GuiSettings.settings().getDisplayDateFormatString().get());
        displayDateFormatValue.addListener((p, o, n) -> handleOnDisplayDateFormatChange(n));
    }

    private void handleOnEditDateFormatChange(String dateFormatString) {
        GuiSettings.settings().setEditDateFormat(dateFormatString);
    }

    private void handleOnDisplayDateFormatChange(String dateFormatString) {
        GuiSettings.settings().setDisplayDateFormat(dateFormatString);
    }
}
