package com.example.spring_security_practice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    private String username;
    private String password;
    private String email;
    private String age;
    private String role;
}
