package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {
    int saveCustomerRemarkByList(List<CustomerRemark> customerRemarkList);
}
