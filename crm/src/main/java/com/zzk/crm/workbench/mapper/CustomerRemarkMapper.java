package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.CustomerRemark;

import java.util.List;

public interface CustomerRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    int insert(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    int insertSelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    CustomerRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    int updateByPrimaryKeySelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbg.generated Tue Sep 26 14:21:08 CST 2023
     */
    int updateByPrimaryKey(CustomerRemark record);

    /**
     * 批量插入CustomerRemark对象
     */
    int insertCustomerRemarkByList(List<CustomerRemark> customerRemarkList);
}