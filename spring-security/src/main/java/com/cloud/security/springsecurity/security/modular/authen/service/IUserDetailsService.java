package com.cloud.security.springsecurity.security.modular.authen.service;

import com.cloud.security.springsecurity.security.modular.authen.model.Register;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义UserDetailsService接口
 * 继承 org.springframework.security.core.userdetails.UserDetailsService
 *
 * @author lijun
 */
public interface IUserDetailsService extends UserDetailsService {

    void register(Register register);

}
