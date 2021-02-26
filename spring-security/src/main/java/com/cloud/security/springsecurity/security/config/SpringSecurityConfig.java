package com.cloud.security.springsecurity.security.config;

import com.cloud.security.springsecurity.security.config.properties.IgnoreUrlProperties;
import com.cloud.security.springsecurity.security.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.modular.authen.handler.AccessDeniedExceptionHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationSuccessHandler;
import com.cloud.security.springsecurity.security.modular.authen.service.IUserDetailsService;
import com.cloud.security.springsecurity.security.modular.validatecode.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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

    @Autowired
    IUserDetailsService userDetailsService;

    @Autowired
    ValidateCodeFilter validateCodeFilter;

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter(authenticationFailureHandler,redisTemplate,securityProperties);
        IgnoreUrlProperties ignoreProperties = securityProperties.getIgnore();
        http.csrf().disable()
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/login.html")
                    .loginProcessingUrl("/authentication/form")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
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
