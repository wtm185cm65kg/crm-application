package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.TranRemark;

import java.util.List;

public interface TranRemarkService {
    int saveTranRemarkByList(List<TranRemark> tranRemarkList);

    List<TranRemark> queryTranRemarkForDetailByTranId(String tranId);
}
