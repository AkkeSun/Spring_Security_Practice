package com.example.spring_security_practice.security.FormLogin.config;

import com.example.spring_security_practice.security.FormLogin.handler.CustomAccessDeniedHandler;
import com.example.spring_security_practice.security.FormLogin.handler.CustomAuthenticationFailureHandler;
import com.example.spring_security_practice.security.FormLogin.handler.CustomAuthenticationSuccessHandler;
import com.example.spring_security_practice.security.FormLogin.provider.CustomAuthenticationProvider;
import com.example.spring_security_practice.security.common.factory.UrlResourcesMapFactoryBean;
import com.example.spring_security_practice.security.common.filter.PermitAllFilter;
import com.example.spring_security_practice.security.common.metadataSource.UrlFilterInvocationSecurityMetadataSource;
import com.example.spring_security_practice.security.common.service.SecurityResourceService;
import com.example.spring_security_practice.security.common.voter.IpAddressVoter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource detailsSource;
    @Autowired
    private SecurityResourceService securityResourceService;

    @Override
    //============ Login처리 ============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
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

        //============ 인증 ============
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(detailsSource)
                .successHandler(customAuthenticationSuccessHandler())
                .failureHandler(customAuthenticationFailureHandler())
                .permitAll()
                .and()
        ;
        //============ 인가 에러 처리 ============
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
        ;

        //============ DB를 활용한 인가처리 Filter 등록 ============
        http
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class) // FilterSecurityInterceptor 앞에 둘 것
        ;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }



    //======== DB를 통해 인가처리를 하기 위한 Filter =========
    //filterSecurityInterceptor에서 PermitAllFilter로 업그레이드
    private String[] permitAllResources = {"/", "/login", "/users/**", "/login*" };
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {

        PermitAllFilter permitAllFilter = new PermitAllFilter();
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource()); // 시큐리티 인가 정보
        permitAllFilter.setAccessDecisionManager(affirmativeBased());      // 접근 결정 매니저
        permitAllFilter.setAuthenticationManager(authenticationManager()); // 인증 매니저
        return permitAllFilter;
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(uriResourcesMapFactoryBean(securityResourceService).getObject(), securityResourceService);
    }
    private UrlResourcesMapFactoryBean uriResourcesMapFactoryBean(SecurityResourceService securityResourceService){
        UrlResourcesMapFactoryBean uriResourcesMapFactoryBean = new UrlResourcesMapFactoryBean(securityResourceService);
        return  uriResourcesMapFactoryBean;
    }

    // 접근결정 매니저
    @Bean
    public AccessDecisionManager affirmativeBased(){
        AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecis());
        return affirmativeBased;
    }

    // 인가 심사
    private List <AccessDecisionVoter<?>> getAccessDecis(){
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new IpAddressVoter()); // ip 심사 (이게 가장 먼저)
        accessDecisionVoters.add(roleVoter());          // Role 계층권한 적용
        return accessDecisionVoters;
    }

    // ---- Role 계층 권한 적용 ----
    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        return roleHierarchyVoter;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        return roleHierarchy;
    }

}
