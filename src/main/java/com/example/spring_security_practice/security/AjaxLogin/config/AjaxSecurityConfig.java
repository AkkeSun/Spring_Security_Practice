package com.example.spring_security_practice.security.AjaxLogin.config;

import com.example.spring_security_practice.security.AjaxLogin.handler.AjaxAccessDeniedHandler;
import com.example.spring_security_practice.security.AjaxLogin.handler.AjaxAuthenticationFailureHandler;
import com.example.spring_security_practice.security.AjaxLogin.handler.AjaxAuthenticationSuccessHandler;
import com.example.spring_security_practice.security.common.AuthenticationEntryPoint.AjaxLoginAuthenticationEntryPoint;
import com.example.spring_security_practice.security.AjaxLogin.filter.AjaxLoginProcessingFilter;
import com.example.spring_security_practice.security.AjaxLogin.provier.AjaxAuthenticationProvider;
import com.example.spring_security_practice.security.common.factory.UrlResourcesMapFactoryBean;
import com.example.spring_security_practice.security.common.metadataSource.UrlFilterInvocationSecurityMetadataSource;
import com.example.spring_security_practice.security.common.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    //============ Login처리 ============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(ajaxAuthenticationProvider());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //============ 인가 ============
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/api/message").hasRole("ADMIN")
                .antMatchers("/api/myPage").hasRole("USER")
                .anyRequest().authenticated()
        ;


        //============ 인증 (ajax 필터) ============
        http
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class); // 기존 필터 앞에 위치하도록


        //============ 인가 에러처리 ============
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint()) // 익명 사용자 접근시
                .accessDeniedHandler(ajaxAjaxAccessDeniedHandler()) // 접근권한 에러 처리
        ;


    }


    @Override
    //============ Filter 커스텀을 위한 매니저 가져오기 ============
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
        return ajaxLoginProcessingFilter;
    }


    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider(){
        return new AjaxAuthenticationProvider();
    }

    @Bean
    public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler(){
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler ajaxAuthenticationFailureHandler(){
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler ajaxAjaxAccessDeniedHandler(){
        return new AjaxAccessDeniedHandler();
    }


}
