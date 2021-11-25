package com.example.spring_security_practice.controller.admin;

import com.example.spring_security_practice.domain.dto.AccountDto;
import com.example.spring_security_practice.domain.dto.ResourcesDto;
import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.service.AccountService;
import com.example.spring_security_practice.service.ResourceService;
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

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/roles")
    public boolean createRoles(@RequestBody RoleDto roleDto) throws Exception {
        roleService.createRole(roleDto);
        return true;
    }

    @PutMapping("/roles/{id}")
    public void roleUpdate(@PathVariable("id") Long id, @RequestBody RoleDto roleDto)  {
        roleService.updateRole(id, roleDto);
    }

    @DeleteMapping("/roles/{id}")
    public boolean RoleDelete(@PathVariable("id") Long id)  {
        roleService.deleteRole(id);
        return true;
    }

    @PostMapping("/accounts")
    public boolean createAccount(@RequestBody AccountDto accountDto)  {
        accountService.createUser(accountDto, true);
        return true;
    }

    @PutMapping("/accounts/{id}")
    public boolean accountUpdate(@PathVariable("id") Long id, @RequestBody AccountDto accountDto)  {
        accountService.updateUser(id, accountDto);
        return true;
    }

    @DeleteMapping("/accounts/{id}")
    public boolean accountDelete(@PathVariable("id") Long id)  {
        accountService.deleteUser(id);
        return true;
    }

    @PostMapping("/resources")
    public boolean createResources(@RequestBody ResourcesDto dto)  {
        System.out.println("CREATE");
        resourceService.createResource(dto);
        return true;
    }

    @PutMapping("/resources/{id}")
    public boolean accountResources(@PathVariable("id") Long id, @RequestBody ResourcesDto dto)  {
        resourceService.updateResources(id, dto);
        return true;
    }

    @DeleteMapping("/resources/{id}")
    public boolean accountResources(@PathVariable("id") Long id)  {
        resourceService.deleteResource(id);
        return true;
    }
}
