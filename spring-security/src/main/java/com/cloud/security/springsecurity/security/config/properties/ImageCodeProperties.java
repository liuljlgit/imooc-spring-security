package com.cloud.security.springsecurity.security.config.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("图片验证码属性配置")
public class ImageCodeProperties {

    @ApiModelProperty("验证码宽度")
    private Integer width = 67;

    @ApiModelProperty("验证码高度")
    private Integer height = 23;

    @ApiModelProperty("多少位验证码")
    private Integer length = 4;

    @ApiModelProperty("过期时间")
    private Integer expireIn = 60;

    @ApiModelProperty("需要进行拦截得url")
    private String url;

}
