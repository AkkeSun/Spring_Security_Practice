package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);
}
