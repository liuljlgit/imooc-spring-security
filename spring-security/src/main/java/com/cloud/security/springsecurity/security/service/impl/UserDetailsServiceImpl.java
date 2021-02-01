package com.cloud.security.springsecurity.security.service.impl;

import com.cloud.ftl.ftlbasic.exception.BusiException;
import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.enums.BoolEnum;
import com.cloud.security.springsecurity.security.entity.SecurityUserDetails;
import com.cloud.security.springsecurity.security.service.IUserDetailsService;
import com.cloud.security.springsecurity.service.ISysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 自定义UserDetailsService接口实现类
 *
 * @author lijun
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements IUserDetailsService {

    @Autowired
    ISysUserService sysUserService;

    @Override
    public SecurityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(username)){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        SysUser sysUserParams = new SysUser();
        sysUserParams.setUserName(username);
        SysUser sysUser = sysUserService.selectOne(sysUserParams);
        if(Objects.isNull(sysUser)){
            throw new UsernameNotFoundException("用户不存在");
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(sysUser.getUserName(), sysUser.getPassword(),
                        BoolEnum.codeMap.getOrDefault(sysUser.getEnabled(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getAccountNonExpired(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getCredentialsNonExpired(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getAccountNonLocked(),true),
                        Lists.newArrayList());
        return securityUserDetails;
    }

}
