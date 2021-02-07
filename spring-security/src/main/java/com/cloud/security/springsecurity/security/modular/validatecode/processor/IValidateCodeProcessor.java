package com.cloud.security.springsecurity.security.modular.validatecode.processor;

import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码处理逻辑
 *
 * @author lijun
 */
public interface IValidateCodeProcessor {

    /**
     * 创建校验码
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request, ValidateCodeType validateCodeType) throws Exception;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void validate(ServletWebRequest servletWebRequest, ValidateCodeType validateCodeType);

}
