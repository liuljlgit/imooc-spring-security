package com.cloud.security.springsecurity.security.validatecode.generator;

import com.cloud.security.springsecurity.security.validatecode.model.ImageCode;
import com.cloud.security.springsecurity.security.validatecode.model.ValidateCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码生成接口
 *
 * @author lijun
 */
public interface IValidateCodeGenerator {

    /**
     * 生成验证码接口
     * @param request
     * @return
     */
    ValidateCode generate(HttpServletRequest request);

}
