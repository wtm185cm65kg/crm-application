package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.settings.service.UserService;
import com.zzk.crm.workbench.pojo.Activity;
import com.zzk.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        /**
         * 调用service层方法所查询所有用户
         * 并在页面请求转发跳转时将查询结果传入request域(不用session域：session太大，该数据仅仅是跳转时携带，没必要不在当前页面时也携带着)
         */
        List<User> users = userService.queryAllUsers();
        request.setAttribute(Constants.REQUEST_USERS,users);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    /**
     * 点击添加/修改/删除，最终展示的页面仍是activity/index.jsp，因此这三个操作也应写在ActivityController中
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        /**由于前端传过来的只是部分数据
         * 并没有id、createTime、createBy属性，因此要手动添加
         * 使用UUID生成UUID并调用其toString()转为字符串，并将生成的所有'-'替换为空串，以获得独一无二的id*/
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateUtil.formatDateTime19(new Date()));
        /**获取session的user，以确定创建者
         * 由于'活动'与'用户'是多对一关系（一个用户可以创建多个活动，一个活动只能由一个用户创建）
         * 因此需要传入的createBy是外键，应传入user的id，而不是其name*/
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //activity.setCreateBy(user.getName());
        activity.setCreateBy(user.getId());

        ReturnJson returnJson = new ReturnJson();
        /**
         * 与查询不同的是，CUD一般要进行try..catch捕捉异常
         */
        try {
            int ret = activityService.saveCreateActivity(activity);

            if (ret>0){//插入成功
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {//插入失败
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                /**
                 * 其实这里就是单纯的后台问题，但为了转移客户的情绪，防止认为我们做的软件不好
                 * 一般都会进行矛盾转移，把后台CUD失败的锅输出信息为'系统忙'
                 * 实际做过开发的人都知道这种输出是后端做的不好，但一般客户并不会怀疑到后端，只是认为系统忙
                 */
                returnJson.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){//插入报异常
            e.printStackTrace();
            /**这里如法炮制上面的做法*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后重试....");
        }
        return returnJson;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        /**beginNo的计算公式放在Controller层封装，不要放到Service层，否则还要取、计算、删除原pageNo，很麻烦*/
        int beginNo = (pageNo-1)*pageSize;
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows=activityService.queryCountOfActivityByCondition(map);

        Map<String,Object> retMap=new HashMap<>();
        retMap.put(Constants.ACTIVITY_LIST,activityList);
        retMap.put(Constants.TOTAL_ROWS,totalRows);

        return retMap;
    }


    @RequestMapping("/workbench/activity/removeActivityByIds.do")
    @ResponseBody
    @Transactional
    /**为了与客户端传过来的属性名一致，这里的参数名不能使用ids，而要使用id.    当然也可以使用@RequestParam手动指定属性名*/
    public Object removeActivityByIds(String[] id){
        ReturnJson returnJson = new ReturnJson();

        /**对于CUD语句要使用try..catch捕捉*/
        try {
            int ret = activityService.removeActivityByIds(id);
            /**ret==-2也在判定成功的范畴：兼容Oracle数据库的批量删除(Oracle批量删除成功返回结果为-2，失败返回-3)*/
            if (ret>0||ret==-2){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                /**经典做法：怨气转移*/
                returnJson.setMessage("系统忙，请联系系统管理员....");
            }
        }catch(Exception e){
            e.printStackTrace();
            /**如法炮制经典做法*/
            returnJson.setMessage("系统忙，请联系系统管理员....");
        }

        return returnJson;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/modifyActivity.do")
    @ResponseBody
    public Object modifyActivity(Activity activity,HttpSession session) {
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        /*封装参数：
            如果做查询条件/参数间不属于一个实体类对象/无实体类的主键id，封装成map
            如果做CUD/参数间属于一个实体类对象且有主键id，封装成实体类对象*/
        activity.setEditTime(DateUtil.formatDateTime19(new Date()));
        //name不唯一，可能会有重名的情况，况且数据库的edit_by字段要求的也是一个外键
        //activity.setEditBy(user.getName());
        activity.setEditBy(user.getId());

        ReturnJson returnJson=new ReturnJson();
        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret == 1){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("系统忙，请联系系统管理员....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJson.setMessage("系统忙，请联系系统管理员....");
        }
        return returnJson;
    }


    /*@RequestMapping("")
    public String update(){

    }*/


}
