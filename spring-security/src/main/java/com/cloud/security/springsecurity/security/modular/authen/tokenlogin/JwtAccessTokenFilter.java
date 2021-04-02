package com.cloud.security.springsecurity.security.modular.authen.tokenlogin;

import com.alibaba.fastjson.JSON;
import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Order(-999)
@Component
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extract(request);
        if(!StringUtils.isEmpty(accessToken)){
            String authenticationJsonStr = redisTemplate.opsForValue().get(SecurityConsts.LOGIN_ACCESS_TOKEN.concat(accessToken));
            if(!StringUtils.isEmpty(authenticationJsonStr)){
                SecurityContextHolder.getContext().setAuthentication(JSON.toJavaObject(JSON.parseObject(authenticationJsonStr), Authentication.class));
            }
        }
        filterChain.doFilter(request,response);
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
