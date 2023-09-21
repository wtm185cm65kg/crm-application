package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Wed Sep 20 18:55:58 CST 2023
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据activityId查询该市场活动所有备注的明细信息
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);

    /**
     * 用户插入备注信息
     */
    int insertActivityRemark(ActivityRemark remark);

    /**
     * 用户删除备注信息
     */
    int deleteActivityRemarkById(String id);

    /**
     * 用户修改备注信息
     */
    int updateActivityRemark(ActivityRemark activityRemark);
}