package be.shad.tl.service;

import java.util.Date;

public interface TimeLogger {

    void startTask(String taskId);
    void startTask(String taskId, Date startDate);

    void stopTask(String taskId);
    void stopTask(String taskId, Date endDate);

    String createTask(String name);
    void tagTask(String taskId, String tagId);

    void setTaskDescription(String taskId, String description);

    void createTag(String code);
    void setTagDescription(String code, String description);
}
