package be.shad.tl.ui.model.event;

public class TaskSelectedEvent implements Event {
    private final String taskId;

    public TaskSelectedEvent(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
