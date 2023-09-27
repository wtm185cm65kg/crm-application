package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.DicValue;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.settings.service.DicValueService;
import com.zzk.crm.settings.service.UserService;
import com.zzk.crm.workbench.pojo.Activity;
import com.zzk.crm.workbench.pojo.Clue;
import com.zzk.crm.workbench.pojo.ClueActivityRelation;
import com.zzk.crm.workbench.pojo.ClueRemark;
import com.zzk.crm.workbench.service.ActivityService;
import com.zzk.crm.workbench.service.ClueActivityRelationService;
import com.zzk.crm.workbench.service.ClueRemarkService;
import com.zzk.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute(Constants.REQUEST_USERS,userList);
        request.setAttribute(Constants.REQUEST_APPELLATIONS,appellationList);
        request.setAttribute(Constants.REQUEST_CLUE_STATES,clueStateList);
        request.setAttribute(Constants.REQUEST_SOURCES,sourceList);

        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtil.formatDateTime19(new Date()));

        ReturnJson returnJson = new ReturnJson();
        try {
            int ret = clueService.saveCreateClue(clue);
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

    @RequestMapping("/workbench/clue/queryClueByCondition.do")
    @ResponseBody
    public Object queryClueByCondition(String fullname,String company,String phone,String mphone,String owner,String source,String state,Integer pageNo,Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        int beginNo = (pageNo-1)*pageSize;
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("state",state);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        List<Clue> clueList = clueService.queryClueByCondition(map);
        int ret = clueService.queryCountOfClue(map);

        Map<String,Object> retMap=new HashMap<>();
        retMap.put(Constants.CLUE_LIST,clueList);
        retMap.put(Constants.TOTAL_ROWS,ret);

        return retMap;
    }

    @RequestMapping("/workbench/clue/toDetailClue.do")
    public String toDetailClue(String id,HttpServletRequest request){
        //先根据线索id拿到线索的详细信息
        Clue clue = clueService.queryClueForDetailById(id);
        //再根据刚刚线索的详细信息的id值拿到对应的线索备注（实际上clue.getId()可以直接写为id）
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(clue.getId());
        //还是根据刚刚线索的详细信息的id值拿到与之相关联的市场活动信息（实际上clue.getId()可以直接写为id）
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(clue.getId());

        //将这些信息发送到request域
        request.setAttribute(Constants.REQUEST_CLUE,clue);
        request.setAttribute(Constants.REQUEST_CLUE_REMARKS,clueRemarkList);
        request.setAttribute(Constants.ACTIVITY_LIST,activityList);

        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameAndClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameAndClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.queryActivityForDetailByNameAndClueId(map);

        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBundleBetweenActivityAndClue.do")
    @ResponseBody
    public Object saveBundleBetweenActivityAndClue(String[] activityId,String clueId){
        //封装参数
        List<ClueActivityRelation> list = new ArrayList<>();
        ClueActivityRelation car = null;
        for (String id:activityId){
            car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(clueId);
            car.setActivityId(id);
            list.add(car);
        }

        //调用service方法，批量插入数据
        ReturnJson returnJson = new ReturnJson();
        try {
            //批量插入市场活动与线索的关系
            int ret = clueActivityRelationService.saveBundleBetweenActivityAndClue(list);

            if (ret>0){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);

                //插入成功则要在页面中回显刚刚与该线索建立联系的活动，应到活动表中查找刚刚勾选的活动
                List<Activity> activityList = activityService.queryActivityByCondition(activityId);
                returnJson.setRetData(activityList);
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

    @RequestMapping("/workbench/clue/saveUnbundleBetweenActivityAndClue.do")
    @ResponseBody
    public Object saveUnbundleBetweenActivityAndClue(ClueActivityRelation clueActivityRelation){
        ReturnJson returnJson = new ReturnJson();
        try {
            //删除市场活动与线索的关系
            int ret = clueActivityRelationService.dropClueActivityRelationByActivityIdAndClueId(clueActivityRelation);

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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String clueId,HttpServletRequest request){
        //根据clueId查询该线索的详细信息
        Clue clue = clueService.queryClueForDetailById(clueId);
        //查询数据字典中的'阶段'对应的值
        List<DicValue> dicValueList = dicValueService.queryDicValueByTypeCode("stage");

        request.setAttribute(Constants.REQUEST_CLUE,clue);
        request.setAttribute(Constants.REQUEST_STAGES,dicValueList);

        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByName.do")
    @ResponseBody
    public Object queryActivityForDetailByName(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.queryActivityForDetailByName(map);

        return activityList;
    }


    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,
                              String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));

        /**由于转存方法中涉及insert语句，因此要try..catch处理转存方法*/
        ReturnJson returnJson = new ReturnJson();
        try {
            /**保存线索转换*/
            clueService.saveConvert(map);

            /**只要没报异常就说明转存成功（因为转存方法返回值为void，不能通过返回值判断是否转存成功）*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            /**报异常说明转存失败*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后再试....");
        }
        return returnJson;
    }
}
