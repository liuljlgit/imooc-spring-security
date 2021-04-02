package com.cloud.security.springsecurity.security.config;

import com.cloud.security.springsecurity.security.config.properties.IgnoreUrlProperties;
import com.cloud.security.springsecurity.security.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.modular.authen.handler.AccessDeniedExceptionHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationSuccessHandler;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import com.cloud.security.springsecurity.security.modular.authen.smslogin.SmsCodeAuthenticationSecurityConfig;
import com.cloud.security.springsecurity.security.modular.authen.tokenlogin.JwtTokenAuthenticationSecurityConfig;
import com.cloud.security.springsecurity.security.modular.validatecode.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    private DataSource dataSource;

    @Resource(name = "usernameLoginUserDetailsService")
    IUserDetailsService usernameLoginUserDetailsService;

    @Autowired
    ValidateCodeFilter validateCodeFilter;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private JwtTokenAuthenticationSecurityConfig jwtTokenAuthenticationSecurityConfig;

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(usernameLoginUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        IgnoreUrlProperties ignoreProperties = securityProperties.getIgnore();
        http.csrf().disable()
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthenticationProvider())
                .formLogin()   //这个其实就是UsernamePasswordAuthenticationFilter.class的配置
                    .loginPage("/login.html")   //配置登录跳转页面
                    .loginProcessingUrl("/authentication/form") //会覆盖UsernamePasswordAuthenticationFilter.class中默认的/login拦截
                    .successHandler(authenticationSuccessHandler)   //成功处理器
                    .failureHandler(authenticationFailureHandler)   //失败处理器
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getRememberMeSeconds())
                    .userDetailsService(usernameLoginUserDetailsService)
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(new AccessDeniedExceptionHandler())
                    //.authenticationEntryPoint(new AuthExceptionHandler())
                .and()
                    .authorizeRequests()
                    .antMatchers(ignoreProperties.getUris().toArray(new String[ignoreProperties.getUris().size()]))
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                    .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                    .apply(jwtTokenAuthenticationSecurityConfig);

    }
}
