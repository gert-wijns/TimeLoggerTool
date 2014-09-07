package be.shad.tl.ui.form;

import be.shad.tl.service.TimeLoggerData;

import com.google.common.eventbus.EventBus;

public class TimeLoggerModel {
    private final TimeLoggerData timeLoggerData;
    private final EventBus eventBus = new EventBus();

    public TimeLoggerModel(TimeLoggerData timeLoggerData) {
        this.timeLoggerData = timeLoggerData;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public TimeLoggerData getTimeLoggerData() {
        return timeLoggerData;
    }
}
