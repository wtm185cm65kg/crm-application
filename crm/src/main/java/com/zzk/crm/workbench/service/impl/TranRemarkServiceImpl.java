package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.TranRemarkMapper;
import com.zzk.crm.workbench.pojo.TranRemark;
import com.zzk.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    @Transactional
    public int saveTranRemarkByList(List<TranRemark> tranRemarkList) {
        return tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
    }

    @Override
    public List<TranRemark> queryTranRemarkForDetailByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
