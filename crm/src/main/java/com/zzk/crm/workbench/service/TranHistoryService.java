package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryService {
    int saveCreateTranHistory(TranHistory tranHistory);

    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
