package com.cloud.security.springsecurity.security.modular.validatecode.sms;

import org.springframework.stereotype.Component;

@Component("SmsCodeSender")
public class ISmsCodeSenderImpl implements ISmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机" + mobile + "发送短信验证码" + code);
    }
}
