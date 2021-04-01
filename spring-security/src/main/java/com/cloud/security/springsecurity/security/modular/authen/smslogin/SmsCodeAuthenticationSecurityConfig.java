package com.cloud.security.springsecurity.security.modular.authen.smslogin;

import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationSuccessHandler;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 短信登录配置
 * @author lijun
 */
@Component
public class SmsCodeAuthenticationSecurityConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;

    @Resource(name = "smsLoginUserDetailsService")
    private IUserDetailsService smsLoginUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(smsLoginUserDetailsService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
