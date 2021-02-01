package com.cloud.security.springsecurity.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum CommonEnum {

    VALID((byte)0,"生效"),
    INVALID((byte)1, "失效"),

    NO((byte)0,"否"),
    YES((byte)1,"是");

    private Byte code;

    private String desc;

    CommonEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
