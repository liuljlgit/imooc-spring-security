package com.cloud.security.springsecurity.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ApiModel("security安全配置")
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    @ApiModelProperty("忽略鉴权配置")
    private IgnoreUrlProperties ignore;

}