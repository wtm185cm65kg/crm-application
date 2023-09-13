package com.zzk.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对Date类型数据进行处理的工具类
 */
public class DateUtil {

    /**
     * 将日期格式转为"yyyy-MM-dd HH:mm:ss"字符串
     */
    public static String formatDateTime19(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 将日期格式一律转为"yyyy-MM-dd"字符串
     */
    public static String formatDateTime10(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

}
