package com.cloud.security.springsecurity.security.validatecode.sms;

/**
 * 短信发送接口
 *
 * @author lijun
 */
public interface SmsCodeSender {

    void send(String mobile,String code);

}
