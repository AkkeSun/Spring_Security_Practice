package com.example.spring_security_practice.controller.login;

import com.example.spring_security_practice.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model){

        // 에러메세지 한글로 바꾸기
        if(exception != null){
            if(exception.contains("Invalid Username or Password"))
                exception = "아이디 혹은 비밀번호가 일치하지 않습니다";
            if(exception.contains("Invalid SecretKey"))
                exception = "시크릿키가 일치하지 않습니다";
        }

        model.addAttribute("error", error);
        model.addAttribute("exception",exception);

        return "user/login/login";
    }


    /**
     * 로그아웃 방법
     * 1. <form> 태그를 사용하는 경우 POST
     * 2. <a> 태그를 사용하는경우 GET : SecurityContextLogoutHandler().logout 사용
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login";
    }


    @GetMapping("/denied")
    public String accessDenied( @RequestParam(value = "exception", required = false) String exception, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);

        return"user/login/denied";
    }


    @GetMapping("/api/denied")
    public String ajaxAccessDenied( @RequestParam(value = "exception", required = false) String exception, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();

        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);

        return"user/login/denied";
    }

}
