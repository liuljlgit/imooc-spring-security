package com.cloud.security.springsecurity.security.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("忽略鉴权的uris")
public class IgnoreUrlProperties {

    @ApiModelProperty("忽略鉴权的uri列表")
    private List<String> uris;

}
