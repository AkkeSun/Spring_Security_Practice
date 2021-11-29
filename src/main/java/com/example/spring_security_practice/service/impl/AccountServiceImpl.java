package com.example.spring_security_practice.service.impl;

import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.AccountRepository;
import com.example.spring_security_practice.repository.RoleRepository;
import com.example.spring_security_practice.security.common.metadataSource.UrlFilterInvocationSecurityMetadataSource;
import com.example.spring_security_practice.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void createUser(AccountDto dto, boolean isAdmin) {

        // role setting
        Set<Role> roles = new HashSet<>();

        if(isAdmin) {
         List<Role> roleList = roleRepository.getRolesForAccount(dto.getRole());
         roleList.forEach( role -> roles.add(role) );
        } else {
            roles.add(roleRepository.findByRoleName("ROLE_USER"));
        }

        Account account = modelMapper.map(dto, Account.class);
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setUserRoles(roles);

        accountRepository.save(account);
    }

    @Transactional
    public void updateUser(Long id, AccountDto dto) {

        Account check = accountRepository.findById(id).get();
        if(check == null)
            throw new NullPointerException("User Not Found");

        // role setting
        Set<Role> roles = new HashSet<>();
        List<Role> roleList = roleRepository.getRolesForAccount(dto.getRole());
        roleList.forEach( role -> roles.add(role) );

        // password setting
        if(dto.getPassword().equals(""))
            dto.setPassword(check.getPassword());
        else
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        // update
        Account account = modelMapper.map(dto, Account.class);
        account.setId(id);
        account.setUserRoles(roles);
        accountRepository.save(account);
    }

    @Transactional
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Transactional
    public Account getUser(Long id) {
        return accountRepository.findById(id).get();
    }

}
