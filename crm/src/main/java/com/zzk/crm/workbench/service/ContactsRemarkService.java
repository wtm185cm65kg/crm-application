package com.zzk.crm.workbench.service;

import com.zzk.crm.workbench.pojo.ContactsRemark;

import java.util.List;

public interface ContactsRemarkService {
    int saveContactsRemarkByList(List<ContactsRemark> contactsRemarkList);
}
