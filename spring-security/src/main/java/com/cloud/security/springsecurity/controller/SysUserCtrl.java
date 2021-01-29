package com.cloud.security.springsecurity.controller;

import com.cloud.ftl.ftlbasic.webEntity.PageBean;
import com.cloud.ftl.ftlbasic.webEntity.RespEntity;
import com.cloud.ftl.ftlbasic.webEntity.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.*;
import com.cloud.security.springsecurity.service.ISysUserService;
import com.cloud.security.springsecurity.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/sysuser")
@Api(tags = "全局接口信息")
public class SysUserCtrl {

    @Autowired
    private ISysUserService sysUserService;

    @GetMapping(value = "/obj")
    @ApiOperation(value = "根据主键查询", notes = "author: llj")
    @ApiImplicitParam(name="uId", value="主键" , required = true)
    public CommonResp<SysUser> selectById(@RequestParam("uId") @NotNull Long uId) {
        return RespEntity.ok(sysUserService.selectById(uId,"没有符合条件的记录！"));
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "查询所有列表", notes = "author: llj")
    public CommonResp<List<SysUser>> selectList(@RequestBody SysUser sysUser){
        return RespEntity.ok(sysUserService.selectList(sysUser));
    }

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询" , notes = "author: llj")
    public CommonResp<PageBean<SysUser>> selectPage(@RequestBody SysUser sysUser) {
        return RespEntity.ok(sysUserService.selectPage(sysUser));
    }


    @PostMapping(value = "/obj")
    @ApiOperation(value = "更新或者新增", notes = "author: llj")
    public CommonResp<Object> save(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return RespEntity.ok();
    }

    @DeleteMapping(value = "/obj")
    @ApiOperation(value = "根据主键删除",tags = "xxx",hidden = true, notes = "author: llj")
    @ApiImplicitParam(name="uId", value="主键",required = true)
    public CommonResp<Object> deleteById(@RequestParam(value="uId") @NotNull Long uId) {
        sysUserService.deleteById(uId);
        return RespEntity.ok();
    }

}
