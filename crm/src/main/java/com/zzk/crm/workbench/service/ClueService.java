package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> queryClueByCondition(Map<String,Object> map);

    int queryCountOfClue(Map<String,Object> map);

    Clue queryClueForDetailById(String id);
}
