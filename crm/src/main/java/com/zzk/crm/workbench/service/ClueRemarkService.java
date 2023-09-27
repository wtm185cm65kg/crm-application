package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    List<ClueRemark> queryClueRemarkByClueId(String clueId);

    int dropClueRemarkByClueId(String clueId);
}
