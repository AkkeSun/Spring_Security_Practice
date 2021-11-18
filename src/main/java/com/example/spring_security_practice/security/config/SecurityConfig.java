package com.example.spring_security_practice.security.config;

import com.example.spring_security_practice.security.handler.CustomAccessDeniedHandler;
import com.example.spring_security_practice.security.handler.CustomAuthenticationFailureHandler;
import com.example.spring_security_practice.security.handler.CustomAuthenticationSuccessHandler;
import com.example.spring_security_practice.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationDetailsSource detailsSource;



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
                .antMatchers("/", "/users", "/login*").permitAll() // login error 처리 추가 -> login*
                .antMatchers("/myPage").hasRole("USER")
                .antMatchers("/message").hasRole("MANAGER")
                .antMatchers("/admin").hasRole("ADMIN")
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

}
