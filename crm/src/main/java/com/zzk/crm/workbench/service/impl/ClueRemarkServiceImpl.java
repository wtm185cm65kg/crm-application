package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ClueRemarkMapper;
import com.zzk.crm.workbench.pojo.ClueRemark;
import com.zzk.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
    }

    @Override
    public List<ClueRemark> queryClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.selectClueRemarkByClueId(clueId);
    }

    @Override
    @Transactional
    public int dropClueRemarkByClueId(String clueId) {
        return clueRemarkMapper.deleteClueRemarkByClueId(clueId);
    }
}
