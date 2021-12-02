package com.example.spring_security_practice.config;

import com.example.spring_security_practice.filter.PermitAllFilter;
import com.example.spring_security_practice.handler.ajax.AjaxAccessDeniedHandler;
import com.example.spring_security_practice.handler.ajax.AjaxAuthenticationFailureHandler;
import com.example.spring_security_practice.handler.ajax.AjaxAuthenticationSuccessHandler;
import com.example.spring_security_practice.AuthenticationEntryPoint.AjaxLoginAuthenticationEntryPoint;
import com.example.spring_security_practice.filter.AjaxLoginProcessingFilter;
import com.example.spring_security_practice.metadataSource.UrlMetadataSource;
import com.example.spring_security_practice.provier.AjaxAuthenticationProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AjaxAuthenticationProvider authenticationProvider;
    @Autowired
    private AjaxAuthenticationSuccessHandler successHandler;
    @Autowired
    private AjaxAuthenticationFailureHandler failureHandler;
    @Autowired
    private AjaxAccessDeniedHandler deniedHandler;
    @Autowired
    private PermitAllFilter customFilterSecurityInterceptor; // FormSecurityConfig 에서 bean 등록

    @Override
    //============ Login처리 ============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //============ 인가 ============
        http
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
        
        //============ DB를 활용한 인가처리 ============
        //기본 사용하는 FilterSecurityInterceptor 앞에 등록
        http
                .addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class)
        ;
        
        //============ 인가 에러처리 ============
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint()) // 익명 사용자 접근시
                .accessDeniedHandler(deniedHandler) 
        ;
        
        //============ 인증 처리 ============
        //기본 사용하는 UsernamePasswordAuthenticationFilter 앞에 등록
        http
                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    //===================== 인증매니저 ======================
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    // ================== Ajax Login Filter ================
    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(successHandler);
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(failureHandler);
        return ajaxLoginProcessingFilter;
    }


}
