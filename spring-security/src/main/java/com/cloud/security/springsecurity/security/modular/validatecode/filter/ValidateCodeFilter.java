package com.cloud.security.springsecurity.security.modular.validatecode.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.security.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.constants.SecurityConst;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
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

    private AuthenticationFailureHandler authenticationFailureHandler;

    private RedisTemplate<String,String> redisTemplate;

    private SecurityProperties securityProperties;

    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler,
                              RedisTemplate<String, String> redisTemplate,SecurityProperties securityProperties) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.redisTemplate = redisTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if(StringUtils.equals(securityProperties.getCode().getImage().getUrl(),request.getRequestURI())
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
        try {
            codeInCache = JSONObject.parseObject(redisTemplate.opsForHash()
                    .get(SecurityConst.IMAGE_CODE_KEY, cacheKey).toString(), ValidateCode.class);
        } catch (Exception e) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("请填写验证码");
        }

        if (Objects.isNull(codeInCache)) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInCache.isExpried()) {
            redisTemplate.opsForHash().delete(SecurityConst.IMAGE_CODE_KEY,cacheKey);
            throw new ValidateCodeException("验证码已过期，请重新获取");
        }

        if (!StringUtils.equals(codeInCache.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确");
        }
        redisTemplate.opsForHash().delete(SecurityConst.IMAGE_CODE_KEY,cacheKey);
    }

}
