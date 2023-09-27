package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.Customer;

import java.util.List;

public interface CustomerService {
    int saveCustomer(Customer customer);

    List<String> blurryQueryCustomerName(String customerName);
}
