package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);

    @Override
    @Query("select r from Role r order by r.roleNum")
    List<Role> findAll();

}