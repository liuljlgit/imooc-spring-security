package com.cloud.security.springsecurity.security.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("短信验证码属性配置")
public class SmsCodeProperties {

    @ApiModelProperty("短信验证码长度")
    private Integer length = 4;

    @ApiModelProperty("过期时间")
    private Integer expireIn = 60;

    @ApiModelProperty("短信验证码拦截URL")
    private String url;

}
