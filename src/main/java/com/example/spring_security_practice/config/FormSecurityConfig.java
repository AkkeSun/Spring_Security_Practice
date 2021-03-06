package com.example.spring_security_practice.config;

import com.example.spring_security_practice.handler.form.FormAccessDeniedHandler;
import com.example.spring_security_practice.handler.form.FormAuthenticationFailureHandler;
import com.example.spring_security_practice.handler.form.FormAuthenticationSuccessHandler;
import com.example.spring_security_practice.provier.FormAuthenticationProvider;
import com.example.spring_security_practice.filter.PermitAllFilter;
import com.example.spring_security_practice.metaDataSource.UrlMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableWebSecurity
@Order(0)
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
    //============ Login?????? ============
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    //============ ???????????? ?????? ============
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                      .antMatchers("/favicon.ico", "/resources/**", "/error");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //============ ?????? ============
        http
                .authorizeRequests()
                /*
                .antMatchers("/", "/users", "/login*").permitAll() // login error ?????? ?????? -> login*
                .antMatchers("/myPage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/admin").hasRole("ADMIN")
                 */
                .anyRequest().authenticated()
        ;

        //============ DB??? ????????? ???????????? ============
        //?????? ???????????? FilterSecurityInterceptor ?????? ??????
        http
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
        ;

        //============ ?????? ?????? ?????? ============
        http
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)
        ;

        //============ ?????? ============
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(detailsSource)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .permitAll()
        ;

    }


    @Override
    //===================== ??????????????? ======================
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    // ================== DB ???????????? Filter ================
    // permitAllFilter : ??????????????? ?????? ?????? get ????????? ????????? ??? ??????
    // permitAllFilter ??? ???????????? ?????? ?????? FilterSecurityInterceptor ??? ???????????? ????????????
    private String[] permitAllResources = {"/", "/login", "/users/**", "/login*"};
    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlMetadataSource);      // url ???????????? ?????? ??????
        permitAllFilter.setAccessDecisionManager(accessDecisionManager);   // ?????? ?????? ?????????
        permitAllFilter.setAuthenticationManager(authenticationManager()); // ?????? ?????????
        return permitAllFilter;
    }
}
