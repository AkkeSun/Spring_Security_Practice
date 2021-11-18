package com.example.spring_security_practice.security.config;

import com.example.spring_security_practice.security.filter.AjaxLoginProcessingFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 *  DSL 설정 ???????????
 *  AbstractAuthenticationFilterConfigurer
 *  필터, 핸들러, 매서드, 속성 등을 한 곳에서 정의할 수 있도록 하는 초기화 클래스
 *  사용 방법 : HttpSecurity.apply()
 */
public class AjaxLoginConfigure <H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, AjaxLoginConfigure<H>, AjaxLoginProcessingFilter> {

    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationManager authenticationManager;


    // ---------- 필터를 생성해서 부모 클래스에 전달 -------
    public AjaxLoginConfigure (){
        super(new AjaxLoginProcessingFilter(), null);
    }

    @Override
    // http에 security 객채가 넘어온다
    public void configure(H http)  {
        if(authenticationManager == null)
            authenticationManager = http.getSharedObject(AuthenticationManager.class);

        getAuthenticationFilter().setAuthenticationManager(authenticationManager);
        getAuthenticationFilter().setAuthenticationSuccessHandler(successHandler);
        getAuthenticationFilter().setAuthenticationFailureHandler(failureHandler);

        // 세션 설정
        SessionAuthenticationStrategy sessionAuthenticationStrategy = http
                .getSharedObject(SessionAuthenticationStrategy.class);

        if(sessionAuthenticationStrategy != null)
            getAuthenticationFilter().setSessionAuthenticationStrategy(sessionAuthenticationStrategy);

        // Remember-Me 설정
        RememberMeServices rememberMeServices = http
                .getSharedObject(RememberMeServices.class);
        if(rememberMeServices != null)
            getAuthenticationFilter().setRememberMeServices(rememberMeServices);


        http.setSharedObject(AjaxLoginProcessingFilter.class, getAuthenticationFilter());
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void init(H http) throws Exception {
        super.init(http);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String s) {
        return null;
    }
}
