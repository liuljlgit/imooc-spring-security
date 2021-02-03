package com.cloud.security.springsecurity.config;

import com.cloud.security.springsecurity.config.properties.IgnoreUrlProperties;
import com.cloud.security.springsecurity.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.handler.AccessDeniedExceptionHandler;
import com.cloud.security.springsecurity.security.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.handler.CustomAuthenticationSuccessHandler;
import com.cloud.security.springsecurity.security.validatecode.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter(authenticationFailureHandler,redisTemplate);
        IgnoreUrlProperties ignoreProperties = securityProperties.getIgnore();
        http.csrf().disable()
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/login.html")
                    .loginProcessingUrl("/authentication/form")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                .and()
                    .exceptionHandling()
                    .accessDeniedHandler(new AccessDeniedExceptionHandler())
                    //.authenticationEntryPoint(new AuthExceptionHandler())
                .and()
                    .authorizeRequests()
                    .antMatchers(ignoreProperties.getUris().toArray(new String[ignoreProperties.getUris().size()]))
                    .permitAll()
                    .anyRequest()
                    .authenticated();

    }
}
