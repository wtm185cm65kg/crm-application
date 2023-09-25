package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbg.generated Sat Sep 23 14:40:51 CST 2023
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
     * 根据线索id查询对应的线索备注
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);
}