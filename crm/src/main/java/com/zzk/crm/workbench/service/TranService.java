package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.Tran;

import java.util.Map;

public interface TranService {
    int saveTran(Tran tran);

    void saveCreateTran(Map<String,Object> map);
}
