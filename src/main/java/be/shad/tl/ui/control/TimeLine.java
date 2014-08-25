package be.shad.tl.ui.control;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TimeLine {
    private DoubleProperty timeScale = new SimpleDoubleProperty(TimeUnit.HOURS.toMillis(1) / 100.0); //800px = 8hr
    private ObjectProperty<Date> startDate = new SimpleObjectProperty<Date>(new Date());
    private ObjectProperty<Date> endDate = new SimpleObjectProperty<Date>(
            new Date(startDate.get().getTime() + TimeUnit.HOURS.toMillis(8)));

    public final DoubleProperty timeScaleProperty() {
        return this.timeScale;
    }

    public final double getTimeScale() {
        return this.timeScaleProperty().get();
    }

    public final void setTimeScale(final double timeScale) {
        this.timeScaleProperty().set(timeScale);
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
