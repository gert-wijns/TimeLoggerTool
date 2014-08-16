package be.shad.tl.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeLoggerViewTag {
    private StringProperty code;
    private StringProperty description;

    public TimeLoggerViewTag(String code, String description) {
        this.code = new SimpleStringProperty(code);
        this.description = new SimpleStringProperty(description);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public StringProperty codeProperty() {
        return code;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
