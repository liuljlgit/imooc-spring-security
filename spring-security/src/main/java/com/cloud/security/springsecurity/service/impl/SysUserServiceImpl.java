package com.cloud.security.springsecurity.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.cloud.security.springsecurity.cache.impl.SysUserCacheImpl;
import com.cloud.security.springsecurity.service.ISysUserService;

/**
 * ISysUserService service实现类
 * @author lijun
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl extends SysUserCacheImpl implements ISysUserService {

}