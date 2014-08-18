package be.shad.tl.service;

import java.util.Date;

public interface TimeLogger {

    void startTask(String taskId);
    void startTask(String taskId, Date startDate);
    void startTask(String taskId, String entryId, Date startDate);

    void stopTask(String taskId);
    void stopTask(String taskId, Date endDate);
    void stopTaskEntry(String entryId, Date startDate);

    String createTask(String name);
    String createTask(String taskId, String name);
    void tagTask(String taskId, String tagId);

    void setTaskDescription(String taskId, String description);

    void createTag(String code);
    void setTagDescription(String code, String description);
    void setTaskName(String taskId, String name);

    void setEntryRemark(String entryId, String remark);
}
