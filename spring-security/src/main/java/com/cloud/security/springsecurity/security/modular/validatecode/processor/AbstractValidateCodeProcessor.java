/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.processor;

import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import com.cloud.security.springsecurity.security.modular.validatecode.generator.IValidateCodeGenerator;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import com.cloud.security.springsecurity.security.modular.validatecode.repository.IValidateCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;
import java.util.Objects;

/**
 * 抽象的图片验证码处理器
 * @author zhailiang
 *
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements IValidateCodeProcessor {

	/**
	 * 收集系统中所有的 {@link IValidateCodeGenerator} 接口的实现。
	 */
	@Autowired
	private Map<String, IValidateCodeGenerator> validateCodeGenerators;

	@Autowired
	private IValidateCodeRepository validateCodeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeProcessor#create(org.
	 * springframework.web.context.request.ServletWebRequest)
	 */
	@Override
	public void create(ServletWebRequest request,ValidateCodeType validateCodeType) throws Exception {
		C validateCode = generate(request,validateCodeType);
		save(request, validateCode,validateCodeType);
		send(request, validateCode);
	}

	/**
	 * 生成校验码
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private C generate(ServletWebRequest request,ValidateCodeType validateCodeType) {
		String generatorName = validateCodeType.toString().toLowerCase() + SecurityConsts.VALIDATE_CODE_GENERATOR_PREFIX_NAME;
		IValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
		if (validateCodeGenerator == null) {
			throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
		}
		return (C) validateCodeGenerator.generate(request);
	}

	/**
	 * 保存校验码
	 * 
	 * @param request
	 * @param validateCode
	 */
	private void save(ServletWebRequest request, C validateCode,ValidateCodeType validateCodeType) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code, validateCodeType);
	}

	/**
	 * 发送校验码，由子类实现
	 * 
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;


	@SuppressWarnings("unchecked")
	@Override
	public void validate(ServletWebRequest request,ValidateCodeType validateCodeType) {

		C codeInRepo = (C) validateCodeRepository.get(request, validateCodeType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
					validateCodeType.getParamNameOnValidate());
		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("获取验证码的值失败");
		}

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException(validateCodeType + "请填写验证码");
		}

		if (Objects.isNull(codeInRepo)) {
			throw new ValidateCodeException(validateCodeType + "验证码不存在");
		}

		if (codeInRepo.isExpried()) {
			validateCodeRepository.remove(request, validateCodeType);
			throw new ValidateCodeException(validateCodeType + "验证码已过期，请重新获取");
		}

		if (!StringUtils.equals(codeInRepo.getCode(), codeInRequest)) {
			throw new ValidateCodeException(validateCodeType + "验证码不正确");
		}

		validateCodeRepository.remove(request, validateCodeType);
		
	}

}
