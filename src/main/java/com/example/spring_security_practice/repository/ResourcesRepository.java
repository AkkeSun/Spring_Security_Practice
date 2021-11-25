package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {
}
