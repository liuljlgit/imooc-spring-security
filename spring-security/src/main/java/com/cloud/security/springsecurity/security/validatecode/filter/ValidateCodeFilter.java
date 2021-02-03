package com.cloud.security.springsecurity.security.validatecode.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.security.validatecode.exception.ValidateCodeException;
import com.cloud.security.springsecurity.security.validatecode.model.ImageCode;
import com.cloud.security.springsecurity.security.validatecode.model.ValidateCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 图片验证码过滤器
 */
@Data
public class ValidateCodeFilter extends OncePerRequestFilter {

    private static final String IMAGE_CODE_KEY = "IMAGE_CODE_KEY";

    private AuthenticationFailureHandler authenticationFailureHandler;

    private RedisTemplate<String,String> redisTemplate;

    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler,
                              RedisTemplate<String, String> redisTemplate) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if(StringUtils.equals("/authentication/form",request.getRequestURI())
            && StringUtils.endsWithIgnoreCase(request.getMethod(),"post")){
            try {
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    private void validate(ServletWebRequest request) {
        String codeInRequest;
        String cacheKey;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");
            cacheKey = ServletRequestUtils.getStringParameter(request.getRequest(),"cacheKey");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        ValidateCode codeInCache = null;
        Object codeObj = redisTemplate.opsForHash().get(IMAGE_CODE_KEY, cacheKey);
        if(Objects.isNull(codeObj)){
            throw new ValidateCodeException("验证码不存在");
        } else {
            codeInCache = JSONObject.parseObject(codeObj.toString(), ValidateCode.class);
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("请填写验证码");
        }


        if (codeInCache == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInCache.isExpried()) {
            redisTemplate.opsForHash().delete(IMAGE_CODE_KEY,cacheKey);
            throw new ValidateCodeException("验证码已过期，请重新获取");
        }

        if (!StringUtils.equals(codeInCache.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确");
        }
        redisTemplate.opsForHash().delete(IMAGE_CODE_KEY,cacheKey);
    }

}
