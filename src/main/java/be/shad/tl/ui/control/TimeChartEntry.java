package be.shad.tl.ui.control;

import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

public class TimeChartEntry extends Control {
    private final ObjectProperty<TimeChartRow> row;
    private final ObjectProperty<Date> startDate;
    private final ObjectProperty<Date> endDate;
    private final ObjectProperty<Object> userData;

    public TimeChartEntry() {
        getStyleClass().add("chart-entry");
        row = new SimpleObjectProperty<>();
        startDate = new SimpleObjectProperty<>();
        endDate = new SimpleObjectProperty<>();
        userData = new SimpleObjectProperty<>();
    }

    public final ObjectProperty<TimeChartRow> rowProperty() {
        return this.row;
    }

    public final be.shad.tl.ui.control.TimeChartRow getRow() {
        return this.rowProperty().get();
    }

    public final void setRow(final be.shad.tl.ui.control.TimeChartRow row) {
        this.rowProperty().set(row);
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

    public final ObjectProperty<Object> userDataProperty() {
        return this.userData;
    }

    public final java.lang.Object getUserData() {
        return this.userDataProperty().get();
    }

    public final void setUserData(final java.lang.Object userData) {
        this.userDataProperty().set(userData);
    }
}
