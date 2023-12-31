package com.zzk.crm.contants;

/**
 * 保存ReturnObject类中的Code值,减小耦合度
 */
public class Constants {
    public static final String RETURN_OBJECT_CODE_SUCCESS = "1";
    public static final String RETURN_OBJECT_CODE_FAIL = "0";
    public static final String REMARK_EDIT_FLAG_NO_EDITED = "0";    //没有被修改过
    public static final String REMARK_EDIT_FLAG_EDITED = "1";   //被修改过

    //放入session域中的当前用户的key
    public static final String SESSION_USER = "sessionUser";
    //放入request域中的查询所有user结果集合的key
    public static final String REQUEST_CLUE = "requestClue";
    public static final String REQUEST_USERS = "requestUsers";
    public static final String REQUEST_CLUES = "requestClues";
    public static final String REQUEST_APPELLATIONS = "requestAppellations";
    public static final String REQUEST_CLUE_STATES = "requestClueStates";
    public static final String REQUEST_SOURCES = "requestSources";
    public static final String REQUEST_STAGES = "requestStages";
    //放入request域中的按id查询activity详细信息的key
    public static final String REQUEST_ACTIVITY = "requestActivity";
    //放入request域中的按activityId查询所有ActivityRemark详细信息的key
    public static final String REQUEST_ACTIVITY_REMARKS = "requestActivityRemarks";
    public static final String REQUEST_CLUE_REMARKS = "requestClueRemarks";
    //queryActivityByConditionForPage()返回的List<Activity>结果集
    public static final String ACTIVITY_LIST = "activityList";
    public static final String CLUE_LIST = "clueList";
    //所有符合queryCountOfActivityByCondition()查询条件的活动数据总条数
    public static final String TOTAL_ROWS = "totalRows";
}
