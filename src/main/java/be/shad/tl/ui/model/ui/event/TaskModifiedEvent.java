package be.shad.tl.ui.model.ui.event;

public class TaskModifiedEvent implements Event {
    private final String taskId;

    public TaskModifiedEvent(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }
}
