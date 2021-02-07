package com.cloud.security.springsecurity.security.modular.validatecode.sms;

/**
 * 短信发送接口
 *
 * @author lijun
 */
public interface ISmsCodeSender {

    void send(String mobile,String code);

}
