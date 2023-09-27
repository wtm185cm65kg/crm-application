package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveBundleBetweenActivityAndClue(List<ClueActivityRelation> list);

    int dropClueActivityRelationByActivityIdAndClueId(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> queryClueActivityRelationByClueId(String clueId);

    int dropClueActivityRelationByClueId(String clueId);
}
