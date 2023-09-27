package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.CustomerMapper;
import com.zzk.crm.workbench.pojo.Customer;
import com.zzk.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    @Transactional
    public int saveCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }

    @Override
    public List<String> blurryQueryCustomerName(String customerName) {
        return customerMapper.selectCustomerNameBlurry(customerName);
    }
}
