package com.zzk.crm.commons.pojo;

public class ReturnJson {
    /**状态码code定义为String类型，通用性强（虽然只有0、1两个值*/
    private String code;  //0--成功  1--失败
    private String message;
    private Object retData;  //返回其他类型数据(这里并不能确定还会返回什么类型的数据，因此将其设为Object)

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
