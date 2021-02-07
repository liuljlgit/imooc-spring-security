package com.cloud.security.springsecurity.security.modular.validatecode.generator;

import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

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
    ValidateCode generate(ServletWebRequest request);

}
