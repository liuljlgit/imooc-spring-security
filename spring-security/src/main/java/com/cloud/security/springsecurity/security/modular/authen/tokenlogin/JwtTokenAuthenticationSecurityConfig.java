package com.cloud.security.springsecurity.security.modular.authen.tokenlogin;

import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationSuccessHandler;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import com.cloud.security.springsecurity.security.modular.authen.smslogin.SmsCodeAuthenticationFilter;
import com.cloud.security.springsecurity.security.modular.authen.smslogin.SmsCodeAuthenticationProvider;
import com.cloud.security.springsecurity.security.modular.authen.tokenlogin.handler.JwtTokenAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.authen.tokenlogin.handler.JwtTokenAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * JwtToken登录配置
 * @author lijun
 */
@Component
public class JwtTokenAuthenticationSecurityConfig
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private JwtTokenAuthenticationSuccessHandler successHandler;

    @Autowired
    private JwtTokenAuthenticationFailureHandler failureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "usernameLoginUserDetailsService")
    private IUserDetailsService usernameLoginUserDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter = new JwtTokenAuthenticationFilter();
        jwtTokenAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        jwtTokenAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        jwtTokenAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

        JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider = new JwtTokenAuthenticationProvider();
        jwtTokenAuthenticationProvider.setUserDetailsService(usernameLoginUserDetailsService);
        jwtTokenAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        http.authenticationProvider(jwtTokenAuthenticationProvider)
                .addFilterAfter(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
