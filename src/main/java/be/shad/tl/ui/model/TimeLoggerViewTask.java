package be.shad.tl.ui.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import be.shad.tl.service.model.TimeLoggerTask;


public class TimeLoggerViewTask {
    private StringProperty id;
    private StringProperty name;
    private StringProperty description;
    private LongProperty duration;
    private BooleanProperty active;
    private BooleanProperty locked;

    public TimeLoggerViewTask(TimeLoggerTask task) {
        this(task.getId(), task.getName(), task.getDescription(), 0L, false, false);
    }

    public TimeLoggerViewTask(String id, String name, String description, Long duration, Boolean active, Boolean locked) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.active = new SimpleBooleanProperty(active);
        this.duration = new SimpleLongProperty(duration);
        this.locked = new SimpleBooleanProperty(locked);
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

    public final StringProperty nameProperty() {
        return this.name;
    }

    public final java.lang.String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final java.lang.String name) {
        this.nameProperty().set(name);
    }

    public final StringProperty descriptionProperty() {
        return this.description;
    }

    public final java.lang.String getDescription() {
        return this.descriptionProperty().get();
    }

    public final void setDescription(final java.lang.String description) {
        this.descriptionProperty().set(description);
    }

    public final BooleanProperty activeProperty() {
        return this.active;
    }

    public final boolean isActive() {
        return this.activeProperty().get();
    }

    public final void setActive(final boolean active) {
        this.activeProperty().set(active);
    }

    public final LongProperty durationProperty() {
        return this.duration;
    }

    public final long getDuration() {
        return this.durationProperty().get();
    }

    public final void setDuration(final long duration) {
        this.durationProperty().set(duration);
    }

    public final BooleanProperty lockedProperty() {
        return this.locked;
    }

    public final boolean isLocked() {
        return this.lockedProperty().get();
    }

    public final void setLocked(final boolean locked) {
        this.lockedProperty().set(locked);
    }
}
