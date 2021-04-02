package com.cloud.security.springsecurity.security.modular.authen.tokenlogin.handler;

import com.cloud.ftl.ftlbasic.webEntity.CommonResp;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        log.error("登录失败",exception);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        String msg = "登录失败，请检查！";
        if(exception instanceof ValidateCodeException){
            msg = "验证码不正确，请重试！";
        }

        CommonResp<String> commonResp = new CommonResp<>();
        commonResp.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        commonResp.setMsg(msg);
        response.getWriter().write(objectMapper.writeValueAsString(commonResp));
    }
}
