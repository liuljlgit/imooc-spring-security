package com.cloud.security.springsecurity.security.modular.validatecode.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.security.constants.SecurityConst;
import com.cloud.security.springsecurity.security.modular.validatecode.generator.IValidateCodeGenerator;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ImageCode;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import com.cloud.security.springsecurity.security.modular.validatecode.sms.ISmsCodeSender;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "验证码模块")
@RestController
public class ValidateCodeCtrl {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private IValidateCodeGenerator imageCodeGenerator;

    @Autowired
    private IValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private ISmsCodeSender smsCodeSender;

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(request);
        String imageCodeKey = UUID.randomUUID().toString();

        ValidateCode validateCode = new ValidateCode();
        validateCode.setCode(imageCode.getCode());
        validateCode.setExpireTime(imageCode.getExpireTime());
        redisTemplate.opsForHash().put(SecurityConst.IMAGE_CODE_KEY, imageCodeKey,JSONObject.toJSONString(validateCode));
        response.setHeader(SecurityConst.IMAGE_CODE_KEY,imageCodeKey);
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request,"mobile");
        ValidateCode smsCode = smsCodeGenerator.generate(request);
        ValidateCode validateCode = new ValidateCode();
        validateCode.setCode(smsCode.getCode());
        validateCode.setExpireTime(smsCode.getExpireTime());
        redisTemplate.opsForHash().put(SecurityConst.SMS_CODE_KEY, mobile,JSONObject.toJSONString(validateCode));
        smsCodeSender.send(mobile,smsCode.getCode());
    }

}
