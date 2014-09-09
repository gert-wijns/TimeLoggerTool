package be.shad.tl.ui;

import static be.shad.tl.util.TimeLoggerUtils.convertEmptyStringToNull;
import static java.util.prefs.Preferences.userRoot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class GuiSettings {
    private static final String DEFAULT_EDIT_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String DEFAULT_DISPLAY_FORMAT = "dd/MM HH:mm";
    private static final String EDIT_DATE_FORMAT_SETTING = "edit-date-format";
    private static final String DISPLAY_DATE_FORMAT_SETTING = "display-date-format";
    private static GuiSettings instance;

    public static GuiSettings settings() {
        if (instance == null) {
            synchronized(GuiSettings.class) {
                if (instance == null) {
                    instance = new GuiSettings();
                }
            }
        }
        return instance;
    }

    private final ReadOnlyObjectWrapper<String> dateFormatString = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<DateFormat> dateFormat = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<String> displayDateFormatString = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<DateFormat> displayDateFormat = new ReadOnlyObjectWrapper<>();

    private GuiSettings() {
        Preferences preferences = getPreferences();
        setDisplayDateFormat(preferences.get(DISPLAY_DATE_FORMAT_SETTING, DEFAULT_DISPLAY_FORMAT));
        setEditDateFormat(preferences.get(EDIT_DATE_FORMAT_SETTING, DEFAULT_EDIT_FORMAT));
    }

    public ReadOnlyObjectProperty<String> getDisplayDateFormatString() {
        return displayDateFormatString.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<DateFormat> getDisplayDateFormat() {
        return displayDateFormat.getReadOnlyProperty();
    }

    public void setDisplayDateFormat(String dateFormatString) {
        dateFormatString = updateDateFormat(DISPLAY_DATE_FORMAT_SETTING, DEFAULT_DISPLAY_FORMAT, dateFormatString);
        this.displayDateFormatString.set(dateFormatString);
        this.displayDateFormatString.get(); // trigger to make value valid
        this.displayDateFormat.set(new SimpleDateFormat(dateFormatString));
        this.displayDateFormat.get(); // trigger to make value valid
    }

    public ReadOnlyObjectProperty<String> getEditDateFormatString() {
        return dateFormatString.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<DateFormat> getEditDateFormat() {
        return dateFormat.getReadOnlyProperty();
    }

    public void setEditDateFormat(String dateFormatString) {
        dateFormatString = updateDateFormat(EDIT_DATE_FORMAT_SETTING, DEFAULT_EDIT_FORMAT, dateFormatString);
        this.dateFormatString.set(dateFormatString);
        this.dateFormatString.get(); // trigger to make value valid
        this.dateFormat.set(new SimpleDateFormat(dateFormatString));
        this.dateFormat.get(); // trigger to make value valid
    }

    public String updateDateFormat(String setting, String defaultDateFormat, String dateFormatString) {
        Preferences preferences = getPreferences();
        if (convertEmptyStringToNull(dateFormatString) == null) {
            dateFormatString = defaultDateFormat;
            preferences.remove(setting);
        } else {
            preferences.put(setting, dateFormatString);
        }
        return dateFormatString;
    }

    private Preferences getPreferences() {
        return userRoot().node(getClass().getName());
    }
}
