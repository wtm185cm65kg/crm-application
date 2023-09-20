package com.zzk.crm.contants;

/**
 * 保存ReturnObject类中的Code值,减小耦合度
 */
public class Constants {
    public static final String RETURN_OBJECT_CODE_SUCCESS = "1";
    public static final String RETURN_OBJECT_CODE_FAIL = "0";

    //放入session域中的当前用户的key
    public static final String SESSION_USER = "sessionUser";
    //放入request域中的查询所有user结果集合的key
    public static final String REQUEST_USERS = "requestUsers";
    //放入request域中的按id查询activity详细信息的key
    public static final String REQUEST_ACTIVITY = "requestUsers";
    //queryActivityByConditionForPage()返回的List<Activity>结果集
    public static final String ACTIVITY_LIST = "activityList";
    //所有符合queryCountOfActivityByCondition()查询条件的活动数据总条数
    public static final String TOTAL_ROWS = "totalRows";
}
