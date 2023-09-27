package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.CustomerRemarkMapper;
import com.zzk.crm.workbench.pojo.CustomerRemark;
import com.zzk.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("customerRemarkService")
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Override
    @Transactional
    public int saveCustomerRemarkByList(List<CustomerRemark> customerRemarkList) {
        return customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
    }
}
