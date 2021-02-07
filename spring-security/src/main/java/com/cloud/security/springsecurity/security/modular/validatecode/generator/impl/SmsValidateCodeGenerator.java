package com.cloud.security.springsecurity.security.modular.validatecode.generator.impl;

import com.cloud.security.springsecurity.security.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.modular.validatecode.generator.IValidateCodeGenerator;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 图片验证码实现类
 *
 * @author lijun
 */
@Component(value = "smsCodeGenerator")
public class SmsValidateCodeGenerator implements IValidateCodeGenerator {

    @Autowired
    SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }

}
