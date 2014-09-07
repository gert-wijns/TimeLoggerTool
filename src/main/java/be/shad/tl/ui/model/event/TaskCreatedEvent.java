package be.shad.tl.ui.model.event;

public class TaskCreatedEvent implements Event {
    private final String taskId;

    public TaskCreatedEvent(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
