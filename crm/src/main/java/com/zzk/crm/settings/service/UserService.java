package com.zzk.crm.settings.service;

import com.zzk.crm.settings.pojo.User;

import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);
}
