package com.cloud.security.springsecurity.controller;

import com.cloud.ftl.ftlbasic.webEntity.CommonResp;
import com.cloud.ftl.ftlbasic.webEntity.PageBean;
import com.cloud.ftl.ftlbasic.webEntity.RespEntity;
import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.model.vo.RegisterVO;
import com.cloud.security.springsecurity.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/security")
@Api(tags = "安全管理模块")
public class SecurityCtrl {

    @Autowired
    private ISysUserService sysUserService;

    @PostMapping(value = "/register")
    @ApiOperation(value = "用户信息注册", notes = "作者: 刘立俊")
    public CommonResp<Object> register(@RequestBody RegisterVO register) {
        sysUserService.register(register);
        return RespEntity.ok();
    }

}
