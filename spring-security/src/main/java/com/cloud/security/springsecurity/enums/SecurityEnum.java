package com.cloud.security.springsecurity.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public enum SecurityEnum {

    MALE((byte)1,"男"),
    FEMALE((byte)2, "女");

    private Byte code;

    private String desc;

    public static Map<Byte, String> codeMap = new HashMap<>();

    public static Map<String, Byte> descMap = new HashMap<>();

    static {
        Arrays.stream(SecurityEnum.values()).forEach(e -> codeMap.put(e.getCode(), e.getDesc()));
        Arrays.stream(SecurityEnum.values()).forEach(e -> descMap.put(e.getDesc(), e.getCode()));
    }

    SecurityEnum(Byte code, String desc) {
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
