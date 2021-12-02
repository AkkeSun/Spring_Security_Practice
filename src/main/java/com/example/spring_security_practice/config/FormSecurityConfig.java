package com.example.spring_security_practice.config;

import com.example.spring_security_practice.handler.form.FormAccessDeniedHandler;
import com.example.spring_security_practice.handler.form.FormAuthenticationFailureHandler;
import com.example.spring_security_practice.handler.form.FormAuthenticationSuccessHandler;
import com.example.spring_security_practice.provier.FormAuthenticationProvider;
import com.example.spring_security_practice.filter.PermitAllFilter;
import com.example.spring_security_practice.metadataSource.UrlMetadataSource;
import com.example.spring_security_practice.voter.IpAddressVoter;
import com.example.spring_security_practice.service.AccessIpService;
import com.example.spring_security_practice.service.security.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
@EnableWebSecurity
@Order(1)
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource detailsSource;
    @Autowired
    private FormAuthenticationProvider authenticationProvider;
    @Autowired
    private FormAuthenticationSuccessHandler successHandler;
    @Autowired
    private FormAuthenticationFailureHandler failureHandler;
    @Autowired
    private FormAccessDeniedHandler deniedHandler;
    @Autowired
    private UrlMetadataSource urlMetadataSource;
    @Autowired
    private AccessDecisionManager accessDecisionManager;

    @Override
    //============ Login처리 ============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    //============ 정적파일 처리 ============
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                      .antMatchers("/favicon.ico", "/resources/**", "/error");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //============ 인가 ============
        http
                .authorizeRequests()
                /*
                .antMatchers("/", "/users", "/login*").permitAll() // login error 처리 추가 -> login*
                .antMatchers("/myPage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/admin").hasRole("ADMIN")
                 */
                .anyRequest().authenticated()
        ;

        //============ DB를 활용한 인가처리 ============
        //기본 사용하는 FilterSecurityInterceptor 앞에 등록
        http
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
        ;

        //============ 인가 에러 처리 ============
        http
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)
        ;

        //============ 인증 ============
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(detailsSource)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
                .and()
        ;


    }


    @Override
    //===================== 인증매니저 ======================
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // ================== DB 인가처리 Filter ================
    // permitAllFilter 를 사용하지 않는 경우 FilterSecurityInterceptor 를 사용한다
    private String[] permitAllResources = {"/", "/login", "/users/**", "/login*" };
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter();
        permitAllFilter.setSecurityMetadataSource(urlMetadataSource);      // 시큐리티 인가 정보
        permitAllFilter.setAccessDecisionManager(accessDecisionManager);   // 접근 결정 매니저
        permitAllFilter.setAuthenticationManager(authenticationManager()); // 인증 매니저
        return permitAllFilter;
    }


}
