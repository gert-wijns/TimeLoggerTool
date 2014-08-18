package be.shad.tl.ui.model;

public class ActiveTaskData {
    private final TimeLoggerViewTask task;
    private final Long startTime;
    private final Long durationBeforeStart;

    public ActiveTaskData(TimeLoggerViewTask task) {
        super();
        this.task = task;
        this.durationBeforeStart = task.getDuration();
        this.startTime = System.currentTimeMillis();
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
