package com.example.spring_security_practice.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {


    @GetMapping("/message")
    public String message() throws Exception{
        return "user/message";
    }

    @ResponseBody
    @GetMapping("/api/message")
    public String apiMessage() throws Exception{
        return "API MESSAGE OK";
    }
}
