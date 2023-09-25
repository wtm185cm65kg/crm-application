package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ClueActivityRelationMapper;
import com.zzk.crm.workbench.pojo.ClueActivityRelation;
import com.zzk.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    @Transactional
    public int saveBundleBetweenActivityAndClue(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertBundleBetweenActivityAndClue(list);
    }

    @Override
    @Transactional
    public int dropClueActivityRelationByActivityIdAndClueId(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.deleteClueActivityRelationByActivityIdAndClueId(clueActivityRelation);
    }
}
