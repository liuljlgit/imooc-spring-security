package com.cloud.security.springsecurity.security.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ApiModel("security安全配置")
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    @ApiModelProperty("忽略鉴权配置类")
    private IgnoreUrlProperties ignore;

    @ApiModelProperty("验证码配置类")
    private ValidateCodeProperties code;

    @ApiModelProperty("记住我持续时间")
    private Integer rememberMeSeconds;

}
