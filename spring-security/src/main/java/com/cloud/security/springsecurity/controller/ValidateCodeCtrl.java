package com.cloud.security.springsecurity.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.constants.SecurityConst;
import com.cloud.security.springsecurity.security.validatecode.generator.IValidateCodeGenerator;
import com.cloud.security.springsecurity.security.validatecode.model.ImageCode;
import com.cloud.security.springsecurity.security.validatecode.model.ValidateCode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Api(tags = "验证码模块")
@RestController
public class ValidateCodeCtrl {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Resource(name = "imageCodeGenerator")
    private IValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = imageCodeGenerator.generate(request);
        String imageCodeKey = UUID.randomUUID().toString();

        ValidateCode validateCode = new ValidateCode();
        validateCode.setCode(imageCode.getCode());
        validateCode.setExpireTime(imageCode.getExpireTime());
        redisTemplate.opsForHash().put(SecurityConst.IMAGE_CODE_KEY, imageCodeKey,JSONObject.toJSONString(validateCode));
        response.setHeader(SecurityConst.IMAGE_CODE_KEY,imageCodeKey);
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

}
