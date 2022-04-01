package com.sp.fc.web.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.Collection;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 스프링의 권한은 AOP 기반으로 동작한다.
    // SecurityFilerChain 당 한개의 FilterSecurityInterceptor를 둘 수 있고, 각 SecurityInterceptor당 한개의 AccessDecisionManager 를 둔다.
    // 반면 Method 권한 판정은 Global 한 권한 위원회를 둔다. 그래서 GlobalMethodSecurityConfiguration 을 통해 AccessDecisionManager 를 설정한다.

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                        .username("user1")
                        .password("1111")
                        .roles("USER")
                )
                ;
    }

    // FilterSecurityInterceptor 는 컨트롤러나 또는 서비스에 들어오는 요청을 가로채서 권한 검증
    // 어떤 페이지나 리소스에 접근하려 할 때 사용자가 접근 권한이 있는지 체크하는 것
    // SecurityInterceptor 는 FilterSecurityInterceptor 와 MethodSecurityInterceptor 로 나뉨
    // 필터이긴 하지만 시큐리티 인터셉터에 기능에 더 가깝다.
    // SecurityConfig 에서 권한 정보를 가져와서 사용한다.
    // AccessDecisionManager 를 통해 권한여부를 판단하고 통과시켜주거나 Deny 한다.
    // 또한 SecurityMetadataSource 라는 인자를 받는다. 각 url에 매핑된 security를 판단하는 근거자료를 가지고 있다.
    // AOP 개념을 따르고 있다.
    // FilterInvocation : 호출한 필터와 인자 정보
    // FilterSecurityInterceptor 에 들어오면 바로 invoke 메소드로 FilterInvocation 를 인자로 받아 proxy 로 동작
    // beforeInvodation : Security Config 에서 설정한 접근 제한을 체크. (거의 애만 동작)
    // finallyInvocation : RunAs 권한을 제거.
    // afterInvocation : AfterInvocationManager 를 통해 체크가 필요한 사항을 체크.
    // 특별히 설정하지 않으면 AfterInvocationManager 는 null.
    FilterSecurityInterceptor filterSecurityInterceptor;

    // SecurityInterceptor -> AccessDecisionManager (decide) -> AccessDecisionVoter -> pass or deny
    // 인증과 비슷하게 AccessDecisionManager 와 AccessDecisionVoter 안에서 supports 메소드를 통해 처리해줄 수 있는지 여부를 확인
    // 근거 자료를 통해 AccessDecisionManager 하위에 voter 들이 request 가 통과할 수 있는지 없는지 위원회를 소집해서 투표를 한다.
    // 이런 로직은 FilterSecurityInterceptor 와 MethodSecurityInterceptor 둘다 동일하다.
    // 권한 체크가 필터에 있냐 메소드에 있냐의 차이
    // 필터단에서 처리할 수 있으면 처리하는게 좋아보임 (디테일한 권한은 컨트롤러나 서비스 안에서 처리)
    AccessDecisionManager filterAccessDecisionManager(){
        return new AccessDecisionManager() {
            @Override
            public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
                throw new AccessDeniedException("접근 금지");
//                return;
            }

            @Override
            public boolean supports(ConfigAttribute attribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return FilterInvocation.class.isAssignableFrom(clazz);
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests( // 이런 권한 설정들이 FilterSecurityInterceptor에서 조사하는 것들
                        authority->authority
                                // antMatchers("/secured")정확한 /secured URL 만 일치
                                // mvcMatchers("/secured") /secured뿐만 아니라 /secured/, /secured.html,/secured.xyz 일치
                                .mvcMatchers("/greeting").hasRole("USER")
                                .anyRequest().authenticated()
//                        .accessDecisionManager(filterAccessDecisionManager())
                )
                ;
    }

    // ExceptionTranslationFilter
    // 권한 오류 발생을 받아 처리하는 필터
    // AuthenticationException(401)과 AccessDeniedException(403)만 처리
    // AuthenticationException 는 AuthenticationEntryPoint 가 처리하고
    // 기본적으로 LoginUrlAuthenticationEntryPoint 이 구현되어 있고, commence 메소드를 통해 로그인 페이지로 redirect 하거나 메시지롤 보낸다.
    // AccessDeniedException 는 AccessDeniedHandler 가 처리한다.
    // 기본적으로 AccessDeniedHandlerImpl 에서 에러페이지를 핸들링 해준다.
    // 직접 우리가 구현해서 사용할 수도 있다.

}

// # 권한 처리에 관여하는 것들
// - 접근하려고 하는 사람이 어떤 접근 권한을 가지고 있는가?
//     - GrantedAuthority
//          Role Based
//          Scope Based
//          User Defined
// - 접근하려고 하는 상황에서는 체크해야 할 내용은 무엇인가?
//     - SecurityMetadataSource, ConfigAttribute
//     - 정적인 경우와 동적인 경우
//     - AccessDecisionVoter 가 vote -> 통과인지 아닌지 투표 (요청에 맞는 voter 들을 기본적으로 넣어주며 내가 직접 구현한 voter 를 추가 가능)
// - 여러가지 판단 결과가 나왔을 때 취합은 어떤 방식으로 할 것인가?
//     - AccessDecisionManager : 권한 위원회
//          AffirmativeBased : 긍정 위원회 (한명이라도 통과로 투표하면 통과, default 인거 같다.)
//          ConsensusBased : 다수결 위원회
//          UnanimouseBased : 만장일치 위원회

// RunAsManager : 임시 권한 부여