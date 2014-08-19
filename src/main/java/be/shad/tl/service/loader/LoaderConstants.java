package be.shad.tl.service.loader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class LoaderConstants {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    // ids
    public static final String ADD_TAG_TO_TASK = "add-tag-to-task";
    public static final String CREATE_TASK = "create-task";
    public static final String REMOVE_ENTRY = "remove-entry";
    public static final String REMOVE_TAG_FROM_TASK = "remove-tag-from-task";
    public static final String SET_TAG_DESCRIPTION = "set-tag-description";
    public static final String SET_TASK_DESCRIPTION = "set-task-description";
    public static final String SET_TASK_NAME = "set-task-name";
    public static final String START_TASK = "start-task";
    public static final String STOP_TASK_ENTRY = "stop-task-entry";
    public static final String SET_ENTRY_REMARK = "set-entry-remark";
    public static final String SET_ENTRY_START_DATE = "set-entry-start-date";
    public static final String SET_ENTRY_END_DATE = "set-entry-end-date";

    // params
    public static final String TASK_ID = "task-id";
    public static final String ENTRY_ID = "entry-id";
    public static final String TAG_ID = "tag-id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start-date";
    public static final String END_DATE = "end-date";
    public static final String REMARK = "remark";
}
