package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> queryClueByCondition(Map<String,Object> map);

    int queryCountOfClue(Map<String,Object> map);

    Clue queryClueForDetailById(String id);

    int dropClueById(String id);

    /**
     * 返回值为void：因为这里面涉及了两个Mapper层的方法，不便通过返回值来判断这两个方法是否都执行成功
     */
    void saveConvert(Map<String,Object> map);
}
