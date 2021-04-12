package com.cloud.security.springsecurity.security.modular.authen.tokenlogin;

import com.alibaba.fastjson.JSON;
import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
public final class CacheSecurityContextRepository implements SecurityContextRepository {

    private RedisTemplate<String,String> redisTemplate;

    public CacheSecurityContextRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return Objects.isNull(getSecurityContext(request));
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();

        SecurityContext context = getSecurityContext(request);
        if (context == null) {
            context = generateNewContext();
        }

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request,
                            HttpServletResponse response) {

    }

    protected SecurityContext generateNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    private SecurityContext getSecurityContext(HttpServletRequest request) {
        String accessToken = extract(request);
        if(!StringUtils.isEmpty(accessToken)){
            String authenticationJsonStr = redisTemplate.opsForValue().get(SecurityConsts.LOGIN_ACCESS_TOKEN.concat(accessToken));
            if(!StringUtils.isEmpty(authenticationJsonStr)){
                Authentication authentication = JSON.toJavaObject(JSON.parseObject(authenticationJsonStr), Authentication.class);
                return new SecurityContextImpl(authentication);
            }
        }
        return null;
    }

    /**
     * 提取请求头中的accessToken
     * @param request
     * @return
     */
    private String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders("Authorization");
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

}