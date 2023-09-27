package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationService {
    int saveContactsActivityRelationByList(List<ContactsActivityRelation> list);
}
