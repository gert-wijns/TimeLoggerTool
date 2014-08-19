package be.shad.tl.service;

import java.util.Date;

public interface TimeLoggerPersistence {

    void startTask(String taskId, String entryId, Date startDate);

    void stopTaskEntry(String entryId, Date endDate);

    void removeEntry(String entryId);

    void createTask(String taskId, String name);

    void setTaskName(String taskId, String name);

    void setTaskDescription(String taskId, String description);

    void setTagDescription(String code, String description);

    void setEntryRemark(String entryId, String remark);

    void setEntryStartDate(String entryId, Date startDate);

    void setEntryEndDate(String entryId, Date endDate);

    void addTagToTask(String taskId, String tagId);

    void removeTagFromTask(String taskId, String tagId);

}
