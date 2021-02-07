/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.processor.impl;

import com.cloud.security.springsecurity.security.constants.SecurityConst;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import com.cloud.security.springsecurity.security.modular.validatecode.processor.AbstractValidateCodeProcessor;
import com.cloud.security.springsecurity.security.modular.validatecode.sms.ISmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码处理器
 * 
 * @author zhailiang
 *
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

	/**
	 * 短信验证码发送器
	 */
	@Autowired
	private ISmsCodeSender smsCodeSender;
	
	@Override
	protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
		String paramName = SecurityConst.DEFAULT_PARAMETER_NAME_MOBILE;
		String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
		smsCodeSender.send(mobile, validateCode.getCode());
	}

}
