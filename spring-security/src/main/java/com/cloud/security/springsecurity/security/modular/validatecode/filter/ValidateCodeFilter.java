package com.cloud.security.springsecurity.security.modular.validatecode.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.security.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.security.constants.SecurityConsts;
import com.cloud.security.springsecurity.security.modular.authen.handler.CustomAuthenticationFailureHandler;
import com.cloud.security.springsecurity.security.modular.validatecode.enums.ValidateCodeType;
import com.cloud.security.springsecurity.security.modular.validatecode.exception.ValidateCodeException;
import com.cloud.security.springsecurity.security.modular.validatecode.model.ValidateCode;
import com.cloud.security.springsecurity.security.modular.validatecode.processor.ValidateCodeProcessorHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.security.util.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 校验验证码的过滤器
 */
@Slf4j
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统配置信息
     */
    @Autowired
    SecurityProperties securityProperties;

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * redisTemplate
     */
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    /**
     * 初始化要拦截的url配置信息
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConsts.DEFAULT_SIGN_IN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConsts.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);
    }

    /**
     * 讲系统中配置的需要校验验证码的URL根据校验的类型放入map
     *
     * @param urlString
     * @param type
     */
    protected void addUrlToMap(String urlString, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlMap.put(url, type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        ValidateCodeType type = getValidateCodeType(request);
        if (Objects.nonNull(type)) {
            log.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                validateCodeProcessorHolder.findValidateCodeProcessor(type)
                        .validate(new ServletWebRequest(request, response) , type);
                log.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }

//    private void validate(ServletWebRequest request) {
//
//        String codeInRequest;
//        String cacheKey;
//        try {
//            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");
//            cacheKey = ServletRequestUtils.getStringParameter(request.getRequest(),"cacheKey");
//        } catch (ServletRequestBindingException e) {
//            throw new ValidateCodeException("获取验证码的值失败");
//        }
//
//        ValidateCode codeInCache = null;
//        try {
//            codeInCache = JSONObject.parseObject(redisTemplate.opsForHash()
//                    .get(SecurityConsts.IMAGE_CODE_KEY, cacheKey).toString(), ValidateCode.class);
//        } catch (Exception e) {
//            throw new ValidateCodeException("验证码不存在");
//        }
//
//        if (StringUtils.isBlank(codeInRequest)) {
//            throw new ValidateCodeException("请填写验证码");
//        }
//
//        if (Objects.isNull(codeInCache)) {
//            throw new ValidateCodeException("验证码不存在");
//        }
//
//        if (codeInCache.isExpried()) {
//            redisTemplate.opsForHash().delete(SecurityConsts.IMAGE_CODE_KEY,cacheKey);
//            throw new ValidateCodeException("验证码已过期，请重新获取");
//        }
//
//        if (!StringUtils.equals(codeInCache.getCode(), codeInRequest)) {
//            throw new ValidateCodeException("验证码不正确");
//        }
//        redisTemplate.opsForHash().delete(SecurityConsts.IMAGE_CODE_KEY,cacheKey);
//    }

}
