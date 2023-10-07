package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    int insert(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    int insertSelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    TranHistory selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    int updateByPrimaryKeySelective(TranHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbg.generated Wed Sep 27 18:49:50 CST 2023
     */
    int updateByPrimaryKey(TranHistory record);

    /**
     * 保存创建的交易历史
     */
    int insertTranHistory(TranHistory tranHistory);

    /**
     * 根据tranId查询交易历史
     */
    List<TranHistory> selectTranHistoryForDetailByTranId(String tranId);
}