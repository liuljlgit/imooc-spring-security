package com.cloud.security.springsecurity.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ApiModel("忽略鉴权的uris")
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreUrlConfig {

    @ApiModelProperty("忽略鉴权的uri列表")
    private List<String> uris;

}
