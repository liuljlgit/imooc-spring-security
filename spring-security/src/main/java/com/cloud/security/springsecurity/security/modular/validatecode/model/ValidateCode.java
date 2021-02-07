/**
 * 
 */
package com.cloud.security.springsecurity.security.modular.validatecode.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("验证码信息封装类")
public class ValidateCode implements Serializable {

	@ApiModelProperty("验证码")
	private String code;

	@ApiModelProperty("过期时间")
	private LocalDateTime expireTime;

	public ValidateCode() {
	}

	public ValidateCode(String code, int expireIn){
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}
	
	public ValidateCode(String code, LocalDateTime expireTime){
		this.code = code;
		this.expireTime = expireTime;
	}
	
	public boolean isExpried() {
		return LocalDateTime.now().isAfter(expireTime);
	}
	
}
