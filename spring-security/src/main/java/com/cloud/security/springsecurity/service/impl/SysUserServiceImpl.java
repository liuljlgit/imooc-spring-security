package com.cloud.security.springsecurity.service.impl;

import com.cloud.ftl.ftlbasic.exception.BusiException;
import com.cloud.security.springsecurity.entity.SysUser;
import com.cloud.security.springsecurity.enums.CommonEnum;
import com.cloud.security.springsecurity.model.vo.RegisterVO;
import com.cloud.security.springsecurity.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cloud.security.springsecurity.cache.impl.SysUserCacheImpl;
import com.cloud.security.springsecurity.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * ISysUserService service实现类
 * @author lijun
 */
@Slf4j
@Service("sysUserService")
public class SysUserServiceImpl extends SysUserCacheImpl implements ISysUserService {

    @Value("${security.register.privateKey}")
    private String decryptKey;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterVO register) {
        try {
            //RSA解密得到明文密码
            String rsaDecryptPass = RsaUtil.privateDecrypt(register.getPassword(), RsaUtil.getPrivateKey(decryptKey));
            //BCrypt加密得到密文
            String bCryptEncoderPass = passwordEncoder.encode(rsaDecryptPass);
            SysUser sysUser = new SysUser();
            BeanUtils.copyProperties(register,sysUser);
            sysUser.setPassword(bCryptEncoderPass);
            sysUser.setCreateTime(new Date());
            this.add(sysUser);
        } catch (Exception e) {
           log.error("用户注册失败",e);
           throw new BusiException("用户注册失败，请联系管理员！");
        }
    }
}