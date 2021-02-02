package com.cloud.security.springsecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "测试模块")
@RestController
public class HelloCtrl {

    @GetMapping("/hello")
    @ApiOperation(value = "测试接口" , notes = "作者：刘立俊")
    public String hello() {
        return "hello spring security";
    }
}
