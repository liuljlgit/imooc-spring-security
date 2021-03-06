package com.cloud.security.springsecuritysocial;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.cloud"})
@MapperScan("cn.cloud.**.dao")
public class SpringSecuritySocialApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecuritySocialApplication.class, args);
    }

}
