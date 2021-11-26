package com.example.spring_security_practice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"accounts","resourcesSet"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Role implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String roleName;

    private String roleDesc;

    private int roleNum;

    @JsonBackReference // 순환참조 방지 (자식 엔티티에 붙이기)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleSet")
    private Set<Resources> resourcesSet = new LinkedHashSet<>();

    @JsonBackReference // 순환참조 방지 (자식 엔티티에 붙이기)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userRoles")
    private Set<Account> accounts = new HashSet<>();

}