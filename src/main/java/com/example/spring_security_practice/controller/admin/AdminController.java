package com.example.spring_security_practice.controller.admin;

import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.service.AccountService;
import com.example.spring_security_practice.service.ResourceService;
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

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public String home() throws Exception {
        return "admin/home";
    }

    //====================== ACCOUNT ======================
    @GetMapping("/accounts")
    public String accounts(Model model) throws Exception {
        model.addAttribute("accounts", accountService.findAll());
        return "admin/user/userList";
    }

    @GetMapping("/accounts/create")
    public String createAccounts(Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("account", new Account());
        return "admin/user/userCreate";
    }

    @GetMapping("/accounts/{id}")
    public String accounts(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("account", accountService.getUser(id));
        return "admin/user/userDetail";
    }

    //====================== ROLE ======================
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

    //====================== RESOURCES ======================
    @GetMapping("/resources")
    public String resources(Model model) throws Exception {
        model.addAttribute("res", resourceService.getResources());
        return "admin/resources/resourceList";
    }

    @GetMapping("/resources/create")
    public String createResources(Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("res", new Resources());
        return "admin/resources/resourceCreate";
    }

    @GetMapping("/resources/{id}")
    public String resources(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("res", resourceService.getResource(id));
        return "admin/resources/resourceDetail";
    }
}
