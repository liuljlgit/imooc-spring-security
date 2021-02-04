package com.cloud.security.springsecurity.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("验证码属性配置")
public class ValidateCodeProperties {

    @ApiModelProperty("图片验证码配置")
    private ImageCodeProperties image;

}
