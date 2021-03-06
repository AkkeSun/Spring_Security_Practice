package com.example.spring_security_practice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String username;
    private String password;
    private String email;
    private int age;
    private String role;
}
