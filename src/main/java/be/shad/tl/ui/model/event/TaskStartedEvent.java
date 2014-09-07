package be.shad.tl.ui.model.event;

public class TaskStartedEvent implements Event {
    private final String taskId;

    public TaskStartedEvent(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
