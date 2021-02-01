package com.cloud.security.springsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class Test {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        log.info("原密码：{}，加密后密码：{}",123456,passwordEncoder.encode("123456"));
    }

}
