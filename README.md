# 인증 처리 (Login)

#### client
     request : username, password, anotherParam
 
 
#### Authentication Filter 
    // AjaxLoginProcessingFilter (구현 필요)
    - ajax login요청이 들어왔을 때 실행  
    - username + password가 담긴 AjaxAuthenticationToken 생성 (구현 필요)
      (인증을 위한 anotherParam이 있는 경우 AuthenticationDetail을 통해 저장 (구현 필요))    
    - details 설정 (접속 IP, Session등이 담겨있음) 
    - token을 AuthenticationManager에게 전달
        
    // UsernamePassword Authentication Filter (시큐리티 자동실행)
    - form login요청이 들어왔을 때 실행 
    - username + password가 담긴 Authentication Token 생성 (시큐리티 기본제공)
      (인증을 위한 anotherParam이 있는 경우 AuthenticationDetail을 통해 저장 (구현 필요))
    - details 설정 (접속 IP, Session등이 담겨있음) 
    - token을 AuthenticationManager에게 전달


#### Authentication Manager (시큐리티 기본제공) 
    Authentication Token 저장
    anotherParam이 있는 경우 저장 
   

#### Authentication Provider (구현 필요)
    // 데이터 불러오기
    Authentication Manager ( Authentication Token ) -> 유저가 입력한 로그인 정보
    (anotherParam이 있는 경우) AuthenticationDetail 
    UserDetailService ( Login DB Data) -> DB에 저장된 유저 정보   
    
    // 유효성 검증
    DB에 저장된 유저정보와 유청한 로그인 정보가 일치한지 검증
    (anotherParam이 있는 경우) anotherParam이 올바른지 검증
    
    // 유효성 검증 결과 처리
    검증이 올바르면 로그인이 성공했다는 데이터가 담긴 Authentication Token 생성후 SecurityContext에 저장
    검증이 실패했다면 예외 발생 
   
    
#### Security Config (구현 필요)
    // 로그인 결과 핸들러 실행 
    성공시 AuthenticationSuccessHandler 실행 
    실패시 AuthenticationFailureHandler 실행
   
   
   
<br/><br/><br/><br/>   
# DB를 통한 인가관리 (접근권한) 

#### entity
    // Account
    @ManyToMany
    Set<Role> userRoles = new HashSet<>();
    
    // Resources 
    @ManyToMany 
    Set<Role> roleSet = new HashSet<>();
    
    //Role (자식) 
    @ManyToMany 
    Set<Resources> resourcesSet = new LinkedHashSet<>();
    @ManyToMany 
    Set<Account> accounts = new HashSet<>();
    
 <br/>  
     
#### URL 방식 : Filter 기반 -> FilterInvocationSecurityMetadataSource(SecurityResourceService) 
    // SecurityResourceService.getResourceList();
    Resources를 LinkedHashMap으로 저장 ( path : RoleList )
    
    // Factory Bean
    SecurityResourceService.getResourceList()를 Factory Bean 으로 생성
    
    // 처리
    사용자가 요청하는 path가 Factory Bean의 key(path)라면 Role(value)을 리턴 
   
#### Method 방식 : AOP 기반 

    // 공통 : securityConfig에 Method보안 활성화 어노테이션 달아주기 
    - @EnableGlobalMethodSecurity
    
    
    // 어노테이션 방식 
    - Service Method에 어노테이션 달아주기 
      @PreAuthorize("hasRole('ROLE_ADMIN')")
      @PostAuthorize("hasRole('ROLE_ADMIN')")
      @Secured("ROLE_ADMIN")
       
    - SecurityConfig에 옵션 추가해주기 
      @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)


    // Map 기반 방식 
    - GlobalMethodSecurityConfiguration을 상속받은 SecurityConfig를 생성한다 
    - Resource를 Map으로 변형한 데이터(MethodResourcesMapFactoryBean)를 MapBasedMethodSecurityMetadataSource로 전달한다. 
    - MapBasedMethodSecurityMetadataSource는 스프링 시큐리티가 기본 제공하는 클래스로 다른 설정을 할 필요가 없다 
    
<br/>       
          
#### 인가 처리 
    // 권한 순서
    1. permitAll()
    2. 좁은 권한 순으로 등록 (admin - admin/manager - admin/manager/user... ) 
    3. anyRequest().authenticated()    
    
    // 권한이 없는 path에 접근 시
    AccessDeniedHandle() 실행 
    
    
<br/>       
          
#### Role 계층 권한 & IP 접근권한 설정  

    // Role 계층권한 설정 
    - 계층권한 적용을 위한 String Format 만들기 
       ROLE_ADMIN > ROLE_MANAGER
       ROLD_MANAGER > ROLE_USER 
       ...
    - String Format을 RoleHierarchy에 담기 (해당 코드는 SecurityInitializer에서 실행)
    - RoleHierarchy을 AccessDecisionVoter에 담기
    
    // IP 접근권한 설정
    - AccessDecisionVoter 
      사용자가 접속한 IP가 허용가능한 IP인지 체크 후 0 or -1 리턴
     
    // AccessDecisionManager (인가처리매니저)
    - AccessDecisionVoter와 IP 심사 결과를 담는다 
<br/>
  

      
<br/><br/><br/><br/>   

        
# 로그인 데이터 가져오기 (SecurityContext Holder)
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserDetails userDetails = (UserDetails) principal;
