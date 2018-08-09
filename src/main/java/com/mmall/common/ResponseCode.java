package com.mmall.common;

public enum ResponseCode {

    /*成功*/
    SUCCESS(0, "SUCCESS"),
    /*失败*/
    ERROR(1, "ERROR"),
    /*需要登录*/
    NEED_LOGIN(10, "需要登录"),
    /*参数错误*/
    ILLEGAL_PARM(2, "参数错误");

    private int code;
    private String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
