package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.workbench.mapper.ClueMapper;
import com.zzk.crm.workbench.pojo.*;
import com.zzk.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private ContactsRemarkService contactsRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private ContactsActivityRelationService contactsActivityRelationService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;

    @Override
    @Transactional
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByCondition(Map<String,Object> map) {
        return clueMapper.selectClueByCondition(map);
    }

    @Override
    public int queryCountOfClue(Map<String,Object> map) {
        return clueMapper.selectCountOfClue(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public int dropClueById(String id) {
        return clueMapper.deleteClueById(id);
    }

    /**
     * 对于数据转存，一般放在Service层，而不放在Controller层，因为放在Controller层会显得很乱
     *
     * 转存方法的返回值为void而不是int：
     *      因为这里面涉及了多个Service层和Mapper层的方法，不方便通过返回值来判断这两个方法是否都执行成功
     *      只能在该service方法中添加事务、在Controller层通过try..catch,只要没报异常就说明转存成功,否则转存失败
     */
    @Override
    @Transactional
    public void saveConvert(Map<String, Object> map) {
        String clueId = (String) map.get("id");
        User user = (User) map.get(Constants.SESSION_USER);

        /**根据clueId查询线索的信息*/
        Clue clue = clueMapper.selectClueById(clueId);
        /**将线索中有关公司的信息取出来封装到客户表中*/
        Customer customer=new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy(user.getId());
        customer.setOwner(user.getId()); //不能是customer.setOwner(clue.getOwner());因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'
        customer.setCreateTime(DateUtil.formatDateTime19(new Date()));
        customer.setName(clue.getCompany());
        customer.setAddress(clue.getAddress());
        customer.setDescription(clue.getDescription());
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        /**将线索中有关联系人的信息取出来封装到联系人表中*/
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCustomerId(customer.getId()); //直接用刚刚封装到Customer对象中的数据
        contacts.setOwner(user.getId()); //不能是customer.setOwner(clue.getOwner());因为要遵循'如果没有明确指定，谁创建的，所有者就是谁'
        contacts.setCreateTime(DateUtil.formatDateTime19(new Date()));
        contacts.setSource(clue.getSource());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setAddress(clue.getAddress());
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        /**将Clue转换成的对象插入到对应的表中（返回值不用处理，可不获取 ）*/
        customerService.saveCustomer(customer);
        contactsService.saveContacts(contacts);


        /**根据clueId查询线索的备注信息*/
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkByClueId(clueId);
        /**将线索备注中的信息取出来封装到客户备注表和联系人备注表中*/
        List<CustomerRemark> customerRemarkList=null;
        List<ContactsRemark> contactsRemarkList=null;
        /**如果该线索有备注，则对备注进行遍历，以进行封装*/
        if (clueRemarkList!=null&&clueRemarkList.size()>0){
            customerRemarkList=new ArrayList<>();
            contactsRemarkList=new ArrayList<>();
            CustomerRemark customerRemark=null;
            ContactsRemark contactsRemark=null;
            //遍历clueRemarkList,将每个ClueRemark转为CustomerRemark和ContactsRemark,并封装到customerRemarkList和contactsRemarkList中
            for (ClueRemark clueRemark:clueRemarkList){
                /**封装CustomerRemark对象，并放入List<CustomerRemark>集合中*/
                customerRemark=new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setCustomerId(customer.getId()); //直接用刚刚封装到Customer对象中的数据
                //对于备注而言，创建/修改者/时间、备注内容都应继承之前的备注信息
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                //将customerRemark封装到customerRemarkList中
                customerRemarkList.add(customerRemark);

                /**封装ContactsRemark对象，并放入List<ContactsRemark>集合中*/
                contactsRemark=new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setContactsId(contacts.getId()); //直接用刚刚封装到Contacts对象中的数据
                //对于备注而言，创建/修改者/时间、备注内容都应继承之前的备注信息
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                //将contactsRemark封装到contactsRemarkList中
                contactsRemarkList.add(contactsRemark);
            }
            /**将ClueRemark转换成的对象插入到对应的表中（返回值不用处理，可不获取 ）*/
            customerRemarkService.saveCustomerRemarkByList(customerRemarkList);
            contactsRemarkService.saveContactsRemarkByList(contactsRemarkList);
        }


        /**根据clueId查询线索与市场活动的关系*/
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationService.queryClueActivityRelationByClueId(clueId);
        /**如果该线索与市场活动有关系，则对关系进行遍历，以进行封装*/
        if (clueActivityRelationList!=null&&clueActivityRelationList.size()>0){
            List<ContactsActivityRelation> list=new ArrayList<>();
            ContactsActivityRelation contactsActivityRelation=null;
            //遍历list,将每个ClueActivityRelation转为ContactsActivityRelation
            for (ClueActivityRelation car:clueActivityRelationList){
                /**封装ContactsActivityRelation对象，并放入List<ContactsActivityRelation>集合中*/
                contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(car.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId()); //直接用刚刚封装到Contacts对象中的数据
                list.add(contactsActivityRelation);
            }
            contactsActivityRelationService.saveContactsActivityRelationByList(list);
        }


        /**判断用户是否勾选了'为客户创建交易'复选框，如果勾选了则要进一步进行操作*/
        String isCreateTran = (String) map.get("isCreateTran");
        if ("true".equals(isCreateTran)){
            /**如果需要创建交易,还要往交易表中添加一条记录（其实交易表中有很多字段,这里只是插入基本信息,毕竟该窗口不是专门处理交易的）*/
            Tran tran=new Tran();
            //需手动添加的字段值
            tran.setId(UUIDUtil.getUUID());
            tran.setOwner(user.getId()); //所有者必须不能为空，总要有一个人负责
            tran.setContactsId(contacts.getId()); //见名知意，应直接用刚刚封装到Contacts对象中的数据
            tran.setCustomerId(customer.getId()); //见名知意，应直接用刚刚封装到Customer对象中的数据
            tran.setCreateBy(user.getId()); //规定创建者为当前用户
            tran.setCreateTime(DateUtil.formatDateTime19(new Date()));
            //可从map中获取的字段值
            tran.setActivityId((String) map.get("activityId"));
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setStage((String) map.get("stage"));
            //保存该交易
            tranService.saveTran(tran);

            /**如果需要创建交易,还要把线索的备注信息（之前已经获取的clueRemarkList）转换到交易备注表（未创建）中*/
            List<TranRemark> tranRemarkList=new ArrayList<>();
            //遍历clueRemarkList,将每个ClueRemark转为TranRemark,并封装到tranRemarkList中
            TranRemark tranRemark=null;
            if (clueRemarkList!=null&&clueRemarkList.size()>0) {
                for (ClueRemark clueRemark : clueRemarkList) {
                    tranRemark = new TranRemark();
                    tranRemark.setId(UUIDUtil.getUUID());
                    tranRemark.setTranId(tran.getId()); //见名知意，应直接用刚刚封装到Tran对象中的数据
                    //对于备注而言，创建/修改者/时间、备注内容都应继承之前的备注信息
                    tranRemark.setNoteContent(clueRemark.getNoteContent());
                    tranRemark.setCreateBy(clueRemark.getCreateBy());
                    tranRemark.setCreateTime(clueRemark.getCreateTime());
                    tranRemark.setEditBy(clueRemark.getEditBy());
                    tranRemark.setEditTime(clueRemark.getEditTime());
                    tranRemark.setEditFlag(clueRemark.getEditFlag());
                    //将tranRemark封装到tranRemarkList中
                    tranRemarkList.add(tranRemark);
                }
                //批量保存交易备注
                tranRemarkService.saveTranRemarkByList(tranRemarkList);
            }
        }


        /**删除线索的备注、线索和市场活动的关联关系、线索
         *
         * 对于多对多关系而言：
         *      删除数据时，先删除子表记录(tbl_xx_xx_relation),再删除父表记录*/
        clueActivityRelationService.dropClueActivityRelationByClueId(clueId); //删除线索和市场活动的关联关系
        clueRemarkService.dropClueRemarkByClueId(clueId); //删除线索的备注
        dropClueById(clueId); //删除线索
    }

    @Override
    public int dropClueByIds(String[] ids) {
        return clueMapper.deleteClueByIds(ids);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int modifyClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }
}
