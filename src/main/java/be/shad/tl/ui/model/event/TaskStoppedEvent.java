package be.shad.tl.ui.model.event;

public class TaskStoppedEvent implements Event {
    private final String taskId;

    public TaskStoppedEvent(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
