package com.cloud.security.springsecurity.service;

import com.cloud.ftl.ftlbasic.service.IBaseCache;
import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.model.vo.RegisterVO;

/**
 * ISysUserService service接口类
 * @author lijun
 */
public interface ISysUserService extends IBaseCache<SysUser>{

    /**
     * 注册对象
     * @param register
     */
    void register(RegisterVO register);

}
