package com.example.spring_security_practice.voter;

import com.example.spring_security_practice.service.AccessIpService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

public class IpAddressVoter implements AccessDecisionVoter {
    
    // @Autowired 하면 실행순서 때문에 Null이 되므로 생성자로 받아온다.
    private AccessIpService accessIpService;

    public IpAddressVoter(AccessIpService accessIpService) {
        this.accessIpService = accessIpService;
    }

    @Override
    public int vote(Authentication authentication, Object o, Collection collection) {

        // 사용자가 접속한 IP 주소
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        // 허용가능한 IP List
        List<String> accessIpList = accessIpService.getAccessIpList();

        int result = ACCESS_DENIED; // -1

        for (String ipAddress : accessIpList) {
            if (ipAddress.equals(remoteAddress))
                return ACCESS_ABSTAIN; // 0
        }

        if (result == ACCESS_DENIED)
            throw new AccessDeniedException("Invalid IpAddress");

        return result;
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return (configAttribute.getAttribute() != null);
    }

    @Override
    public boolean supports(Class aClass) {
        return true;
    }
}

