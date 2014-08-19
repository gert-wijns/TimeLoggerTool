package be.shad.tl.ui.model;

import java.util.Date;

public class ActiveTaskData {
    private final TimeLoggerViewTask task;
    private final Long startTime;
    private final Long durationBeforeStart;

    public ActiveTaskData(TimeLoggerViewTask task, Date startDate) {
        this.task = task;
        this.durationBeforeStart = task.getDuration();
        this.startTime = startDate.getTime();
    }

    public TimeLoggerViewTask getTask() {
        return task;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getDuration() {
        return durationBeforeStart + (System.currentTimeMillis() - startTime);
    }
}
