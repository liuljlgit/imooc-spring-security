package com.cloud.security.springsecurity.security.modular.authen.controller;

import com.cloud.ftl.ftlbasic.webEntity.CommonResp;
import com.cloud.ftl.ftlbasic.webEntity.RespEntity;
import com.cloud.security.springsecurity.security.modular.authen.model.Register;
import com.cloud.security.springsecurity.security.modular.authen.service.IRegisterService;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@RequestMapping("/security")
@Api(tags = "安全管理模块")
public class UserDetailsCtrl {

    @Autowired
    private IRegisterService registerService;

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户信息注册", notes = "作者: 刘立俊")
    public CommonResp<Object> register(@RequestBody Register register) {
        registerService.register(register);
        return RespEntity.ok();
    }

}
