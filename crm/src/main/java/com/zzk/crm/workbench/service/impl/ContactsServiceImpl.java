package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ContactsMapper;
import com.zzk.crm.workbench.pojo.Contacts;
import com.zzk.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    @Transactional
    public int saveContacts(Contacts contacts) {
        return contactsMapper.insertContacts(contacts);
    }
}
