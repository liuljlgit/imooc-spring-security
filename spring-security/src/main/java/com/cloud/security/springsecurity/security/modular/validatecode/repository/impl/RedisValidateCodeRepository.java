/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.repository.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import com.cloud.security.springsecurity.security.modular.validatecode.repository.IValidateCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 * 
 * @author zhailiang
 *
 */
@Slf4j
@Component
public class RedisValidateCodeRepository implements IValidateCodeRepository {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeRepository#save(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * com.imooc.security.core.validate.code.ValidateCode,
	 * com.imooc.security.core.validate.code.ValidateCodeType)
	 */
	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
		ValidateCode validateCode = new ValidateCode();
		validateCode.setCode(code.getCode());
		validateCode.setExpireTime(code.getExpireTime());
		redisTemplate.opsForValue().set(buildKey(request, type), JSON.toJSONString(validateCode), 30, TimeUnit.MINUTES);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeRepository#get(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * com.imooc.security.core.validate.code.ValidateCodeType)
	 */
	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
		Object value = redisTemplate.opsForValue().get(getKey(request, type));
		if (value == null) {
			return null;
		}
		return JSON.toJavaObject(JSON.parseObject(value.toString()),ValidateCode.class);
		//return (ValidateCode) value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.imooc.security.core.validate.code.ValidateCodeRepository#remove(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * com.imooc.security.core.validate.code.ValidateCodeType)
	 */
	@Override
	public void remove(ServletWebRequest request, ValidateCodeType type) {
		redisTemplate.delete(getKey(request, type));
	}


	private String buildKey(ServletWebRequest request, ValidateCodeType type) {
		String deviceId = UUID.randomUUID().toString();
		log.info("后台验证码生成得deviceId：" + deviceId);
		return deviceId;
	}

	/**
	 * @param request
	 * @param type
	 * @return
	 */
	private String getKey(ServletWebRequest request, ValidateCodeType type) {
		String deviceId;
		try{
			deviceId = ServletRequestUtils.getRequiredStringParameter((ServletRequest) request, "deviceId");
			if(StringUtils.isBlank(deviceId)){
				throw new RuntimeException();
			}
		} catch (Exception e){
			throw new ValidateCodeException("请在请求中携带deviceId参数");
		}
//		String deviceId = request.getHeader("deviceId");
//		if (StringUtils.isBlank(deviceId)) {
//			throw new ValidateCodeException("请在请求头中携带deviceId参数");
//		}
		return "code:" + type.toString().toLowerCase() + ":" + deviceId;
	}

}
