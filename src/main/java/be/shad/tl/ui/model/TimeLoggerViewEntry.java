package be.shad.tl.ui.model;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import be.shad.tl.service.model.TimeLoggerEntry;

public class TimeLoggerViewEntry {
    private StringProperty id;
    private ObjectProperty<Date> startDate;
    private ObjectProperty<Date> endDate;
    private ObjectProperty<Long> duration;
    private StringProperty remark;

    public TimeLoggerViewEntry(TimeLoggerEntry entry) {
        this(entry.getId(), entry.getStartDate(), entry.getEndDate(), entry.getRemark());
    }

    public TimeLoggerViewEntry(String id, Date startDate, Date endDate, String remark) {
        this.id = new SimpleStringProperty(id);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.remark = new SimpleStringProperty(remark);
        this.duration = new SimpleObjectProperty<>(endDate == null ? null: endDate.getTime() - startDate.getTime());
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

    public final StringProperty remarkProperty() {
        return this.remark;
    }

    public final java.lang.String getRemark() {
        return this.remarkProperty().get();
    }

    public final void setRemark(final java.lang.String remark) {
        this.remarkProperty().set(remark);
    }

    public final ObjectProperty<Long> durationProperty() {
        return this.duration;
    }

    public final Long getDuration() {
        return this.durationProperty().get();
    }

    public final void setDuration(final Long duration) {
        this.durationProperty().set(duration);
    }
}
