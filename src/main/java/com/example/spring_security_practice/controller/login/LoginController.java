package com.example.spring_security_practice.controller.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String login() {
        return "user/login/login";
    }


    /**
     * 로그아웃 방법
     * 1. <form> 태그를 사용하는 경우 POST
     * 2. <a> 태그를 사용하는경우 GET : SecurityContextLogoutHandler 사용
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login";
    }

}
