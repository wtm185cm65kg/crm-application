package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.DicValue;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.settings.service.DicValueService;
import com.zzk.crm.settings.service.UserService;
import com.zzk.crm.workbench.pojo.Tran;
import com.zzk.crm.workbench.service.CustomerService;
import com.zzk.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TransactionController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service方法，查询动态数据
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        //调用service方法，查询动态数据
        List<User> userList = userService.queryAllUsers();
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStageName.do")
    @ResponseBody
    public Object getPossibilityByStageName(String stageName,HttpServletRequest request){
        //解析配置文件，获得阶段对应的可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageName);

        //将获得的阶段对应的可能性返回给前端
        return possibility;
    }

    @RequestMapping("/workbench/transaction/blurryQueryCustomerName.do")
    @ResponseBody
    public Object blurryQueryCustomerName(String customerName){
        //获取根据输入结果获取与之匹配的客户名称
        List<String> customerNameList = customerService.blurryQueryCustomerName(customerName);

        //将获得的客户名称返回给前端
        return customerNameList;
    }

    /*@RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam
                                 Map<String,Object> map, HttpSession session){
        // 封装参数
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));

        //调用service方法，保存创建的交易
        ReturnJson returnJson = new ReturnJson();
        try {
            tranService.saveCreateTran(map);

            *//**只要没报异常就说明保存成功（因为保存方法返回值为void，不能通过返回值判断是否转存成功）*//*
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            *//**报异常说明保存失败*//*
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后再试....");
        }
        return returnJson;
    }*/

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(String customerName, HttpSession session, Tran tran){
        //对数据tran进行简单处理，将整个Tran对象封装到map中
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtil.formatDateTime19(new Date()));

        // 封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("customerName",customerName);
        map.put(Constants.SESSION_USER,user);
        map.put("tran",tran);

        //调用service方法，保存创建的交易
        ReturnJson returnJson = new ReturnJson();
        try {
            tranService.saveCreateTran(map);

            /**只要没报异常就说明保存成功（因为保存方法返回值为void，不能通过返回值判断是否转存成功）*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            /**报异常说明保存失败*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后再试....");
        }
        return returnJson;
    }
}
