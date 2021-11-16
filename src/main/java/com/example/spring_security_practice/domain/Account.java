package com.example.spring_security_practice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private String age;
    private String role;

}
