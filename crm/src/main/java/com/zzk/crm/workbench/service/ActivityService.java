package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int removeActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    List<Activity> queryAllActivity();

    List<Activity> queryActivityByCondition(String[] ids);

    int saveActivityByList(List<Activity> activityList);

    Activity queryActivityForDetailById(String id);
}
