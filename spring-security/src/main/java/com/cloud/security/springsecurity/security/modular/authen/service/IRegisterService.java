package com.cloud.security.springsecurity.security.modular.authen.service;

import com.cloud.security.springsecurity.security.modular.authen.model.Register;

/**
 * 注册类服务
 * @author lijun
 */
public interface IRegisterService {

    /**
     * 用户注册
     * @param register
     */
    void register(Register register);

}
