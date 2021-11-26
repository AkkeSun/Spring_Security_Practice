package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.queryDSL.CommonQUeryDSL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, CommonQUeryDSL {

    Role findByRoleName(String name);

    @Query("select r from Role r order by r.roleNum")
    List<Role> findAll();

}