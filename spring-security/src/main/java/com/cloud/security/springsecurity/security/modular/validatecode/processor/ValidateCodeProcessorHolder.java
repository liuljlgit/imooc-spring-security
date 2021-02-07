/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.processor;

import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 校验码处理器管理器
 * 
 * @author lijun
 *
 */
@Component
public class ValidateCodeProcessorHolder {

	@Autowired
	private Map<String, IValidateCodeProcessor> validateCodeProcessors;

	/**
	 * @param type
	 * @return
	 */
	public IValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * @param type
	 * @return
	 */
	public IValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + IValidateCodeProcessor.class.getSimpleName();
		IValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}

}
