package com.zzk.crm.workbench.mapper;

import com.zzk.crm.commons.pojo.FunnelIVO;
import com.zzk.crm.workbench.pojo.Tran;

import java.util.List;

public interface TranMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbg.generated Tue Sep 26 18:43:23 CST 2023
     */
    int updateByPrimaryKey(Tran record);

    /**
     * 查询所有的交易明细（关联表）
     */
    List<Tran> selectAllTran();

    /**
     * 保存创建的交易
     */
    int insertTran(Tran tran);

    /**
     *根据id查询交易明细（关联表）
     */
    Tran selectTranForDetailById(String id);

    /**
     * 依据阶段stage,分组查询各个阶段的交易数量
     */
    List<FunnelIVO> selectCountOfTranGroupByStage();
}