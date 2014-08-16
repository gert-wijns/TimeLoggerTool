package be.shad.tl.ui.model;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeLoggerViewEntry {
    private StringProperty id;
    private ObjectProperty<Date> startDate;
    private ObjectProperty<Date> endDate;

    public TimeLoggerViewEntry(String id, Date startDate, Date endDate) {
        this.id = new SimpleStringProperty(id);
        this.startDate = new SimpleObjectProperty<Date>(startDate);
        this.endDate = new SimpleObjectProperty<Date>(endDate);
    }

    public final StringProperty idProperty() {
        return this.id;
    }

    public final java.lang.String getId() {
        return this.idProperty().get();
    }

    public final void setId(final java.lang.String id) {
        this.idProperty().set(id);
    }

    public final ObjectProperty<Date> startDateProperty() {
        return this.startDate;
    }

    public final java.util.Date getStartDate() {
        return this.startDateProperty().get();
    }

    public final void setStartDate(final java.util.Date startDate) {
        this.startDateProperty().set(startDate);
    }

    public final ObjectProperty<Date> endDateProperty() {
        return this.endDate;
    }

    public final java.util.Date getEndDate() {
        return this.endDateProperty().get();
    }

    public final void setEndDate(final java.util.Date endDate) {
        this.endDateProperty().set(endDate);
    }
}
