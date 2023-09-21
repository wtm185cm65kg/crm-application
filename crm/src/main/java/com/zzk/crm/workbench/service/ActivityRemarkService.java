package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    int saveCreateActivityRemark(ActivityRemark remark);

    int removeActivityRemarkById(String id);

    int saveModifyActivityRemark(ActivityRemark activityRemark);
}
