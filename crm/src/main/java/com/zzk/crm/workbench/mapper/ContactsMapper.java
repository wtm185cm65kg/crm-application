package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.Contacts;

public interface ContactsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    int insert(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    int insertSelective(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    Contacts selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    int updateByPrimaryKeySelective(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbg.generated Tue Sep 26 11:47:18 CST 2023
     */
    int updateByPrimaryKey(Contacts record);

    /**
     * 保存创建的联系人
     */
    int insertContacts(Contacts contacts);
}