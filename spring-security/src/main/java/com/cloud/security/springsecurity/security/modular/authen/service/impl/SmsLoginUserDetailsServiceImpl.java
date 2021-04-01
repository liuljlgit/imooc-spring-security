package com.cloud.security.springsecurity.security.modular.authen.service.impl;

import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.enums.BoolEnum;
import com.cloud.security.springsecurity.enums.SecurityEnum;
import com.cloud.security.springsecurity.security.modular.authen.model.SecurityUserDetails;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import com.cloud.security.springsecurity.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 自定义短信登录接口实现类
 *
 * @author lijun
 */
@Slf4j
@Service("smsLoginUserDetailsService")
public class SmsLoginUserDetailsServiceImpl implements IUserDetailsService {

    @Autowired
    ISysUserService sysUserService;

    @Override
    public SecurityUserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(phone)){
            throw new UsernameNotFoundException("手机号不能为空");
        }
        SysUser sysUserParams = new SysUser();
        sysUserParams.setPhone(Long.valueOf(phone));
        SysUser sysUser = sysUserService.selectOne(sysUserParams);
        if(Objects.isNull(sysUser)){
            throw new UsernameNotFoundException("用户不存在");
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(
                        sysUser.getUId(),sysUser.getRealName(),sysUser.getBirthday(),
                        SecurityEnum.codeMap.get(sysUser.getSex()),sysUser.getCreateTime(),
                        sysUser.getUserName(), sysUser.getPassword(),
                        BoolEnum.codeMap.getOrDefault(sysUser.getEnabled(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getAccountNonExpired(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getCredentialsNonExpired(),true),
                        BoolEnum.codeMap.getOrDefault(sysUser.getAccountNonLocked(),true),
                        AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user"));
        return securityUserDetails;
    }

}
