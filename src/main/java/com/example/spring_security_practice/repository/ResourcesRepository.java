package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.entity.Resources;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {


    @EntityGraph(value = "getRole")
    @Query("select r from Resources r order by r.orderNum")
    List<Resources> findAllResources();

    @EntityGraph(value = "getRole")
    @Query("select r from Resources r where r.resourceType = 'url' order by r.orderNum desc")
    List<Resources> findAllUrlResources();
}
