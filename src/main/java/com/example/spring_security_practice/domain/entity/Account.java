package com.example.spring_security_practice.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private int age;

    @JsonManagedReference // 순환참조 방지 (부모 엔티티에 붙이기)
    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinTable(name = "account_roles", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = {
            @JoinColumn(name = "roleId") })
    private Set<Role> userRoles = new HashSet<>();

}