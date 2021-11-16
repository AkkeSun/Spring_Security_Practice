package com.example.spring_security_practice.repository;

import com.example.spring_security_practice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
