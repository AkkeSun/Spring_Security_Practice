package com.example.spring_security_practice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto{

    private String roleName;
    private String roleDesc;
    private int roleNum;

}