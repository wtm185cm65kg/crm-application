package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.TranRemark;

import java.util.List;

public interface TranRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    int insert(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    int insertSelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    TranRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    int updateByPrimaryKeySelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbg.generated Tue Sep 26 19:13:56 CST 2023
     */
    int updateByPrimaryKey(TranRemark record);

    /**
     * 批量保存交易备注
     */
    int insertTranRemarkByList(List<TranRemark> tranRemarkList);
}