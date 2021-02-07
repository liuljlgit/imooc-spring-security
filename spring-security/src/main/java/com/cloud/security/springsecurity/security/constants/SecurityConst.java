package com.cloud.security.springsecurity.security.constants;

/**
 * 安全配置常量类
 *
 * @author lijun
 */
public interface SecurityConst {

    String IMAGE_CODE_KEY = "IMAGE_CODE_KEY";

    String SMS_CODE_KEY = "SMS_CODE_KEY";

    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";

}
