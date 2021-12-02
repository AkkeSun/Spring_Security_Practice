package com.example.spring_security_practice.setup;

import com.example.spring_security_practice.domain.entity.Resources;
import com.example.spring_security_practice.domain.entity.AccessIp;
import com.example.spring_security_practice.domain.entity.Account;
import com.example.spring_security_practice.domain.entity.Role;
import com.example.spring_security_practice.repository.AccessIpRepository;
import com.example.spring_security_practice.repository.AccountRepository;
import com.example.spring_security_practice.repository.ResourcesRepository;
import com.example.spring_security_practice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Runner 실행 전 기본 데이터 입력 이벤트
 */
@Component
public class SecurityPostListener implements ApplicationListener<ApplicationStartedEvent> {

    //================= 데이터 입력 =================

    // ROLE
    private final String ROLE_NAME = "ROLE_ADMIN";
    private final String ROLE_DESC = "관리자";

    // USER
    private final String USER_NAME = "admin";
    private final String PASSWORD = "1111";

    // RESOURCE
    private final String RESOURCE_NAME = "/admin/**";
    private final String RESOURCE_TYPE = "url";
    private final String HTTP_METHOD = "";

    // IP (Localhost)
    private final String IP = "0:0:0:0:0:0:0:1";

    //=================================================


    @Autowired
    private AccessIpRepository accessIpRepository;

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {

        createRoleIfNotFound(ROLE_NAME, ROLE_DESC);
        createUserIfNotFound(USER_NAME, PASSWORD, ROLE_NAME);
        createResourceIfNotFound(RESOURCE_NAME, HTTP_METHOD, RESOURCE_TYPE, ROLE_NAME);
        setupAccessIpData(IP);

    }

    @Transactional
    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .build();
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Account createUserIfNotFound(String userName, String password, String roleName) {

        Account account = accountRepository.findByUsername(userName);

        if (account == null) {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findByRoleName(roleName));

            account = Account.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        return accountRepository.save(account);
    }

    @Transactional
    public Resources createResourceIfNotFound(String resourceName, String httpMethod, String resourceType, String roleName) {
        Resources resources = resourcesRepository.findByResourceNameAndHttpMethod(resourceName, httpMethod);

        if (resources == null) {

            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findByRoleName(roleName));

            resources = Resources.builder()
                    .resourceName(resourceName)
                    .roleSet(roleSet)
                    .httpMethod(httpMethod)
                    .resourceType(resourceType)
                    .orderNum(0)
                    .build();
        }
        return resourcesRepository.save(resources);
    }

    @Transactional
    public void setupAccessIpData(String ip) {
        AccessIp byIpAddress = accessIpRepository.findByIpAddress(ip);
        if (byIpAddress == null) {
            AccessIp accessIp = AccessIp.builder()
                    .ipAddress(ip)
                    .build();
            accessIpRepository.save(accessIp);
        }
    }

}
