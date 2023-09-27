package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ContactsRemarkMapper;
import com.zzk.crm.workbench.pojo.ContactsRemark;
import com.zzk.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("contactsRemarkService")
public class ContactsRemarkServiceImpl implements ContactsRemarkService {
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Override
    @Transactional
    public int saveContactsRemarkByList(List<ContactsRemark> contactsRemarkList) {
        return contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
    }
}
