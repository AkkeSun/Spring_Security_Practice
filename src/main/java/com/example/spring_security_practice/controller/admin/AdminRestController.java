package com.example.spring_security_practice.controller.admin;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.service.AccountService;
import com.example.spring_security_practice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/roles")
    public boolean roles(@RequestBody RoleDto roleDto) throws Exception {
        roleService.createRole(roleDto);
        return true;
    }

    @PutMapping("/roles/{id}")
    public Role roleUpdate(@PathVariable("id") Long id, @RequestBody RoleDto roleDto)  {
        Role role = roleService.updateRole(id, roleDto);
        return role;
    }

    @DeleteMapping("/roles/{id}")
    public boolean RoleDelete(@PathVariable("id") Long id)  {
        roleService.deleteRole(id);
        return true;
    }

    @PutMapping("/accounts/{id}")
    public Account accountUpdate(@PathVariable("id") Long id, @RequestBody AccountDto accountDto)  {

        Account account = accountService.updateUser(id, accountDto);
        return account;
    }

    @DeleteMapping("/accounts/{id}")
    public boolean accountDelete(@PathVariable("id") Long id)  {
        accountService.deleteUser(id);
        return true;
    }
}
