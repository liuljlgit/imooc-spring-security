package com.cloud.security.springsecurity.security.modular.authen.tokenlogin.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.ftl.ftlbasic.webEntity.CommonResp;
import com.cloud.ftl.ftlbasic.webEntity.RespEntity;
import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.error("登录成功",authentication);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        String accessToken = UUID.randomUUID().toString().replace("-","");
        String accessTokenCacheKey = SecurityConsts.LOGIN_ACCESS_TOKEN.concat(accessToken);
        redisTemplate.opsForValue().set(accessTokenCacheKey , JSON.toJSONString(authentication));
        //返回accessToken
        JSONObject object = new JSONObject();
        object.put("accessToken",accessToken);
        response.getWriter().write(objectMapper.writeValueAsString(RespEntity.ok(object)));
    }
}
