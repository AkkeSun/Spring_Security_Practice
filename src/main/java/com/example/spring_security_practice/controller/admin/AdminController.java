package com.example.spring_security_practice.controller.admin;


import com.example.spring_security_practice.domain.dto.RoleDto;
import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.service.AccountService;
import com.example.spring_security_practice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String home() throws Exception {
        return "admin/home";
    }

    @GetMapping("/accounts")
    public String accounts(Model model) throws Exception {
        model.addAttribute("accounts", accountService.findAll());
        return "admin/user/userList";
    }

    @GetMapping("/accounts/{id}")
    public String accounts(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("account", accountService.getUser(id));
        return "admin/user/userDetail";
    }

    @GetMapping("/roles")
    public String roles(Model model) throws Exception {
        model.addAttribute("roles", roleService.getRoles());
        return "admin/role/roleList";
    }

    @GetMapping("/roles/{id}")
    public String roles(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("role", roleService.getRole(id));
        return "admin/role/roleDetail";
    }

    @GetMapping("/roles/create")
    public String createRoles(Model model) throws Exception {
        model.addAttribute("role", new Role());
        return "admin/role/roleCreate";
    }


    @GetMapping("/resources")
    public String resources() throws Exception {
        return "admin/resources/resources";
    }

}
