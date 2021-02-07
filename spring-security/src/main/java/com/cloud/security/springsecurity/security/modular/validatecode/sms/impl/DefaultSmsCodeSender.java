package com.cloud.security.springsecurity.security.modular.validatecode.sms.impl;

import com.cloud.security.springsecurity.security.modular.validatecode.sms.ISmsCodeSender;
import org.springframework.stereotype.Component;

@Component("SmsCodeSender")
public class DefaultSmsCodeSender implements ISmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机" + mobile + "发送短信验证码" + code);
    }
}
