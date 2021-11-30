package com.example.spring_security_practice.security.common.filter;

import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 누구나 접근가능한 path를 설정하는 필터
 * FilterInvocationSecurityMetadataSource 에서 자원을 확인하기 전에 걸러버리기
 */
public class PermitAllFilter extends FilterSecurityInterceptor {

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
    private boolean observeOncePerRequest = true;

    private List<RequestMatcher> permitAllRequestMatcher = new ArrayList<>();

    // 생성자로 인증이 필요없는 path를 받은 후 List에 저장
    public PermitAllFilter(String ...permitAllResources){
        for(String resurece : permitAllResources){
            permitAllRequestMatcher.add(new AntPathRequestMatcher(resurece));
        }
    }

    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        
        boolean permitAll = false;
        HttpServletRequest request = ((FilterInvocation)object).getRequest(); // 유저가 요청한 path

        // 유저가 요청한 path가 인증이 필요없는 path인지 검사
         for(RequestMatcher requestMatcher : permitAllRequestMatcher){
             if(requestMatcher.matches(request)){
                 permitAll = true;
                 break;
             }
         }
        // 권한심사 하지 않겠다.
         if(permitAll)
             return null;
        // 권한심사 하겠다.
        return super.beforeInvocation(object);
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        if (fi.getRequest() != null && fi.getRequest().getAttribute("__spring_security_filterSecurityInterceptor_filterApplied") != null && this.observeOncePerRequest) {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            if (fi.getRequest() != null && this.observeOncePerRequest) {
                fi.getRequest().setAttribute("__spring_security_filterSecurityInterceptor_filterApplied", Boolean.TRUE);
            }

            InterceptorStatusToken token = super.beforeInvocation(fi);

            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            } finally {
                super.finallyInvocation(token);
            }

            super.afterInvocation(token, (Object)null);
        }

    }

    public boolean isObserveOncePerRequest() {
        return this.observeOncePerRequest;
    }

    public void setObserveOncePerRequest(boolean observeOncePerRequest) {
        this.observeOncePerRequest = observeOncePerRequest;
    }
}
