package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.workbench.mapper.CustomerMapper;
import com.zzk.crm.workbench.mapper.TranMapper;
import com.zzk.crm.workbench.pojo.Customer;
import com.zzk.crm.workbench.pojo.Tran;
import com.zzk.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    @Transactional
    public int saveTran(Tran tran) {
        return tranMapper.insertTran(tran);
    }

    /**写法一：将所有参数全部封装到map中，统一处理*/
    /*@Override
    @Transactional
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerName");
        User user = (User) map.get(Constants.SESSION_USER);

        *//**如果客户表中没有该客户（根据客户全名称判断），则先创建一个新客户再创建交易(因为必须提供客户的id)
         * 如果有该客户，则直接创建交易*//*
        Customer customer = customerMapper.selectCustomerByName(customerName);
        //没有该客户则新建客户再创建交易
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(user.getId()); //因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'
            customer.setOwner(user.getId()); //因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'，且owner一定不能为空
            customer.setCreateTime(DateUtil.formatDateTime19(new Date()));
            customer.setName(customerName);  *//**创建的新客户的名称就是customerName*//*
            customerMapper.insertCustomer(customer);
        }
        //有该客户，则直接创建交易
        Tran tran = new Tran();
        tran.setId(UUIDUtil.getUUID());
        tran.setOwner((String) map.get("owner"));  //该owner从map中取，而不是使用当前用户作为owner，因为前端提供了owner选项
        tran.setCustomerId(customer.getId());
        tran.setCreateBy(user.getId());
        tran.setCreateTime(DateUtil.formatDateTime19(new Date()));
        tran.setActivityId((String) map.get("activityId"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setMoney((String) map.get("money"));
        tran.setName((String) map.get("name"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setStage((String) map.get("stage"));
        tran.setType((String) map.get("type"));
        tran.setSource((String) map.get("source"));
        tran.setDescription((String) map.get("description"));
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        //创建交易
        tranMapper.insertTran(tran);
    }*/

    /**写法二：将部分参数封装到map中，一些数据在Controller层进行简单处理*/
    @Override
    @Transactional
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerName");
        User user = (User) map.get(Constants.SESSION_USER);

        /**如果客户表中没有该客户（根据客户全名称判断），则先创建一个新客户再创建交易(因为必须提供客户的id)
         * 如果有该客户，则直接创建交易*/
        Customer customer = customerMapper.selectCustomerByName(customerName);
        //没有该客户则新建客户再创建交易
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateBy(user.getId()); //因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'
            customer.setOwner(user.getId()); //因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'，且owner一定不能为空
            customer.setCreateTime(DateUtil.formatDateTime19(new Date()));
            customer.setName(customerName);  /**创建的新客户的名称就是customerName*/
            customerMapper.insertCustomer(customer);
        }
        //有该客户，则直接创建交易
        Tran tran = (Tran) map.get("tran");
        tran.setCustomerId(customer.getId());

        //创建交易
        tranMapper.insertTran(tran);
    }
}
