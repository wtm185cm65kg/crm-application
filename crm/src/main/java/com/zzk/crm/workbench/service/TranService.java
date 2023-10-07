package com.zzk.crm.workbench.service;

import com.zzk.crm.commons.pojo.FunnelIVO;
import com.zzk.crm.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    int saveTran(Tran tran);

    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<Tran> queryAllTran();

    List<FunnelIVO> queryCountOfTranGroupByStage();
}
