package com.zzk.crm.workbench.mapper;

import com.zzk.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbg.generated Thu Sep 21 19:47:35 CST 2023
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 保存创建的线索
     */
    int insertClue(Clue clue);

    /**
     * 根据条件查询符合条件的线索
     */
    List<Clue> selectClueByCondition(Map<String,Object> map);

    /**
     * 根据条件查询符合条件的线索的总条数
     */
    int selectCountOfClue(Map<String,Object> map);

    /**
     * 根据id查询对应的线索（关联表）
     */
    Clue selectClueForDetailById(String id);

    /**
     * 根据id查询对应的线索（不关联表）
     */
    Clue selectClueById(String id);
}