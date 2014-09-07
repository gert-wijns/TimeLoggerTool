package be.shad.tl.ui.model;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeLoggerViewEntry {
    private StringProperty taskId;
    private StringProperty taskName;
    private StringProperty entryId;
    private StringProperty remark;
    private ObjectProperty<Date> startDate;
    private ObjectProperty<Date> endDate;
    private ObjectProperty<Long> duration;

    public TimeLoggerViewEntry() {
        taskId = new SimpleStringProperty();
        taskName = new SimpleStringProperty();
        remark = new SimpleStringProperty();
        entryId = new SimpleStringProperty();
        startDate = new SimpleObjectProperty<>();
        endDate = new SimpleObjectProperty<>();
        duration = new SimpleObjectProperty<>();
    }

    public final StringProperty taskIdProperty() {
        return this.taskId;
    }

    public final java.lang.String getTaskId() {
        return this.taskIdProperty().get();
    }

    public final void setTaskId(final java.lang.String taskId) {
        this.taskIdProperty().set(taskId);
    }

    public final StringProperty taskNameProperty() {
        return this.taskName;
    }

    public final java.lang.String getTaskName() {
        return this.taskNameProperty().get();
    }

    public final void setTaskName(final java.lang.String taskName) {
        this.taskNameProperty().set(taskName);
    }

    public final StringProperty entryIdProperty() {
        return this.entryId;
    }

    public final java.lang.String getEntryId() {
        return this.entryIdProperty().get();
    }

    public final void setEntryId(final java.lang.String entryId) {
        this.entryIdProperty().set(entryId);
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

    public final ObjectProperty<Long> durationProperty() {
        return this.duration;
    }

    public final Long getDuration() {
        return this.durationProperty().get();
    }

    public final void setDuration(final Long duration) {
        this.durationProperty().set(duration);
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
}
