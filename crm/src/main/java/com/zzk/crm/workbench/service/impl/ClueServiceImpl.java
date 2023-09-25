package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ClueMapper;
import com.zzk.crm.workbench.pojo.Clue;
import com.zzk.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Override
    @Transactional
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByCondition(Map<String,Object> map) {
        return clueMapper.selectClueByCondition(map);
    }

    @Override
    public int queryCountOfClue(Map<String,Object> map) {
        return clueMapper.selectCountOfClue(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }
}
