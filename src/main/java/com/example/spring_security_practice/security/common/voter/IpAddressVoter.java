package com.example.spring_security_practice.security.common.voter;

import com.example.spring_security_practice.security.common.service.SecurityResourceService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

public class IpAddressVoter implements AccessDecisionVoter {

    private SecurityResourceService securityResourceService;

    @Override
    public int vote(Authentication authentication, Object o, Collection collection) {

        // 사용자의 IP 주소 얻기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails)authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();
        System.err.println(remoteAddress);

        // 허용가능한 IP List
        List<String> accessIpList = securityResourceService.getAccessIpList();

        int result = ACCESS_DENIED;

        for(String ipAddress : accessIpList){
            if(ipAddress.equals(remoteAddress))
                return ACCESS_ABSTAIN; // 0
        }

        if(result==ACCESS_DENIED)
            throw new AccessDeniedException("Invalid IpAddress");

        return result;
    }


    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class aClass) {
        return true;
    }
}
