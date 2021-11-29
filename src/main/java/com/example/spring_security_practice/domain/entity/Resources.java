package com.example.spring_security_practice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@ToString(exclude = {"roleSet"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(name = "getRole", attributeNodes = @NamedAttributeNode("roleSet"))
public class Resources implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String resourceName;

    private String httpMethod;

    private int orderNum;

    private String resourceType;

    @JsonBackReference // 순환참조 방지 (부모엔티티에 붙이기)
    @ManyToMany
    @JoinTable(name = "role_Resources", joinColumns = {
            @JoinColumn(name = "resourceId") }, inverseJoinColumns = { @JoinColumn(name = "roleId") })
    private Set<Role> roleSet = new HashSet<>();
}