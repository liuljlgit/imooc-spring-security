package com.cloud.security.springsecurity.security.constants;

/**
 * 安全配置常量类
 *
 * @author lijun
 */
public interface SecurityConsts {

    String IMAGE_CODE_KEY = "IMAGE_CODE_KEY";

    String SMS_CODE_KEY = "SMS_CODE_KEY";

    /**
     * 默认的处理验证码的url前缀
     */
    String VALIDATE_CODE_PROCESSOR_PREFIX_NAME = "ValidateCodeProcessor";

    /**
     * 默认的处理验证码的url前缀
     */
    String VALIDATE_CODE_GENERATOR_PREFIX_NAME = "ValidateCodeGenerator";

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

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

    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/authentication/form";

    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE = "/authentication/mobile";

}
