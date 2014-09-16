package be.shad.tl.service;

import java.util.Date;

public interface TimeLogger {

    String startTask(String taskId, Date startDate);

    void stopTask(String taskId);

    void stopTask(String taskId, Date endDate);

    /**
     * @return taskId of the created task
     */
    String createTask(String name);

    void startTask(String taskId, String entryId, Date startDate);

    void stopTaskEntry(String entryId, Date startDate);

    void removeEntry(String entryId);

    void createTag(String code);

    String createTask(String taskId, String name);

    void setTaskName(String taskId, String name);

    void setTaskDescription(String taskId, String description);

    void setTagDescription(String code, String description);

    void setEntryRemark(String entryId, String remark);

    void setEntryStartDate(String entryId, Date startDate);

    void setEntryEndDate(String entryId, Date endDate);

    void addTagToTask(String taskId, String tagId);

    void removeTagFromTask(String taskId, String tagId);

    String getEntriesAsCsvString();

    void changeTask(String entryId, String taskId);
}
