package com.zzk.crm.commons.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * 获取UUID的String
     * UUID.randomUUID()可以生成一个独一无二的UUID,返回值类型为UUID
     * UUID.randomUUID().toString()是为了将UUID转为String类型
     * UUID.randomUUID().toString().replaceAll("-","") 是因为自动生成的UUID中间会有'-',想将'-'去除需要使用replaceAll("-",""),且去除后长度才为32位,才符合字段要求
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
