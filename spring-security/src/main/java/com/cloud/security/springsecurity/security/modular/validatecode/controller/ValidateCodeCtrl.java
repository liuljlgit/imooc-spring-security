/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.controller;

import com.cloud.ftl.ftlbasic.exception.BusiException;
import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import com.cloud.security.springsecurity.security.modular.validatecode.processor.ValidateCodeProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import sun.security.util.SecurityConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 生成校验码的请求处理器
 * 
 * @author lijun
 *
 */
@RestController
public class ValidateCodeCtrl {

	@Autowired
	private ValidateCodeProcessorHolder validateCodeProcessorHolder;

	/**
	 * 创建验证码，根据验证码类型不同，调用不同的 ValidateCodeProcessor 接口实现
	 *
	 * @param request
	 * @param response
	 * @param type
	 * @throws Exception
	 */
	@GetMapping(SecurityConsts.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
	public void createCode(HttpServletRequest request, HttpServletResponse response,
						   @PathVariable String type) throws Exception {
		ValidateCodeType validateCodeType = ValidateCodeType.validateCodeTypeMap.getOrDefault(type, null);
		if(Objects.isNull(validateCodeType)){
			throw new BusiException("获取验证码类型：" + type + "不正确");
		}
		validateCodeProcessorHolder.findValidateCodeProcessor(validateCodeType)
				.create(new ServletWebRequest(request, response),validateCodeType);
	}


}
