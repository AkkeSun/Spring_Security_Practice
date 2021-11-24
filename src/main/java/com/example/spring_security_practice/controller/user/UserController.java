package com.example.spring_security_practice.controller.user;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private AccountService service;

    @GetMapping("/myPage")
    public String myPage(){
        return "user/myPage";
    }

    @ResponseBody
    @PostMapping("/api/myPage")
    public String apiMessage() throws Exception{
        return "API MYPAGE OK";
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
