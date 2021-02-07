/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.enums;

import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;

/**
 * 
 * 校验码类型
 * 
 * @author zhailiang
 *
 */
public enum ValidateCodeType {
	
	/**
	 * 短信验证码
	 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConsts.DEFAULT_PARAMETER_NAME_CODE_SMS;
		}
	},

	/**
	 * 图片验证码
	 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConsts.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
		}
	};

	/**
	 * 校验时从请求中获取的参数的名字
	 * @return
	 */
	public abstract String getParamNameOnValidate();

	/**
	 * 根据枚举名称获取枚举
	 */
	public static Map<String, ValidateCodeType> validateCodeTypeMap = Maps.newHashMap();

	/**
	 * 存储枚举名称对应得枚举类
	 */
	static {
		Arrays.stream(ValidateCodeType.values())
				.forEach(e -> validateCodeTypeMap.put(e.toString().toLowerCase(), e));
	}

}
