package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbg.generated Wed Sep 13 20:12:59 CST 2023
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 插入一个市场活动
     */
    int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动的列表
     */
    List<Activity>  selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动的总条数
     */
    int selectCountOfActivityByCondition(Map<String, Object> map);

    /**
     *根据id(批量)删除市场活动
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查找市场活动(用于打开'更新模态窗口'时查询信息)
     */
    Activity selectActivityById(String id);

    /**
     * 根据id更新市场活动信息
     */
    int updateActivity(Activity activity);

    /**
     * 查询所有市场活动
     */
    List<Activity> selectAllActivity();

    /**
     * 根据id查询市场活动
     */
    List<Activity> selectActivityByCondition(String[] ids);

    /**
     * 批量插入市场活动
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * 根据id查询市场活动信息
     */
    Activity selectActivityForDetailById(String id);

    /**
     * 根据clueId查询该线索相关联的市场活动信息
     */
    List<Activity> selectActivityForDetailByClueId(String clueId);

    /**
     * 根据name和clueId查询没有与clueId相关联的市场活动信息
     */
    List<Activity> selectActivityForDetailByNameAndClueId(Map<String,Object> map);

    /**
     * 根据id批量查询市场活动的信息
     */
    List<Activity> selectActivityForDetailByIds(String[] ids);

    /**
     * 根据名称模糊查询市场活动，且该市场活动是在当前线索相关联的市场活动中的
     */
    List<Activity> selectActivityForDetailByName(Map<String,Object> map);
}