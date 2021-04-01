package com.cloud.security.springsecurity.security.modular.authen.service.impl;

import com.cloud.ftl.ftlbasic.exception.BusiException;
import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.security.modular.authen.model.Register;
import com.cloud.security.springsecurity.security.modular.authen.service.IRegisterService;
import com.cloud.security.springsecurity.service.ISysUserService;
import com.cloud.security.springsecurity.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class RegisterServiceImpl implements IRegisterService {

    @Value("${security.register.privateKey}")
    private String decryptKey;

    @Autowired
    ISysUserService sysUserService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(Register register) {
        try {
            //RSA解密得到明文密码
            String rsaDecryptPass = RsaUtil.privateDecrypt(register.getPassword(), RsaUtil.getPrivateKey(decryptKey));
            //BCrypt加密得到密文
            String bCryptEncoderPass = passwordEncoder.encode(rsaDecryptPass);
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(register,sysUser);
            sysUser.setPassword(bCryptEncoderPass);
            sysUser.setCreateTime(new Date());
            sysUserService.add(sysUser);
        } catch (Exception e) {
            log.error("用户注册失败",e);
            throw new BusiException("用户注册失败，请联系管理员！");
        }
    }

}
