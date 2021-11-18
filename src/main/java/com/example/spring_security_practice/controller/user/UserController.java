package com.example.spring_security_practice.controller.user;

import com.example.spring_security_practice.domain.Account;
import com.example.spring_security_practice.domain.AccountDto;
import com.example.spring_security_practice.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private AccountService service;

    @GetMapping("/myPage")
    public String myPage(){
        return "user/myPage";
    }


    @GetMapping("/users")
    public String createUser(Model model) {
        model.addAttribute("accountDto", new AccountDto());
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto dto) {
        service.createUser(dto);
        return "redirect:/";
    }

}
