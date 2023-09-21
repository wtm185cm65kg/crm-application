package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.workbench.pojo.ActivityRemark;
import com.zzk.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        //二次封装参数
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtil.formatDateTime19(new Date()));
        remark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnJson returnJson = new ReturnJson();
        try {
            int ret = activityRemarkService.saveCreateActivityRemark(remark);
            if (ret==1){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnJson.setRetData(remark);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后重试....");
        }

        return returnJson;
    }

    @RequestMapping("/workbench/activity/removeActivityRemarkById.do")
    @ResponseBody
    public Object removeActivityRemarkById(String id){
        ReturnJson returnJson = new ReturnJson();

        try {
            int ret = activityRemarkService.removeActivityRemarkById(id);
            if (ret==1){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("系统忙，请稍后再试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后再试....");
        }

        return returnJson;
    }

    @RequestMapping("/workbench/activity/saveModifyActivityRemark.do")
    @ResponseBody
    public Object saveModifyActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        //二次封装参数
        activityRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_EDITED);
        activityRemark.setEditTime(DateUtil.formatDateTime19(new Date()));
        activityRemark.setEditBy(user.getId());

        ReturnJson returnJson = new ReturnJson();
        try {
            int ret = activityRemarkService.saveModifyActivityRemark(activityRemark);
            if (ret==1){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnJson.setRetData(activityRemark);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("系统忙，请稍后再试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后再试....");
        }
        return returnJson;
    }
}
