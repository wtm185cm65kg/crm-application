package com.zzk.crm.workbench.service.impl;

import com.zzk.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.zzk.crm.workbench.pojo.ContactsActivityRelation;
import com.zzk.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("contactsActivityRelationService")
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Override
    @Transactional
    public int saveContactsActivityRelationByList(List<ContactsActivityRelation> list) {
        return contactsActivityRelationMapper.insertContactsActivityRelationByList(list);
    }
}
