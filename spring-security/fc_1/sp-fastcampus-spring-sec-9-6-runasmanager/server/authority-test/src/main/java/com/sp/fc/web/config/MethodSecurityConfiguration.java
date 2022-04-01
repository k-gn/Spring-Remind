package com.sp.fc.web.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;


@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    MethodSecurityInterceptor methodSecurityInterceptor;
    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return new CustomMetadataSource();
    }

    // 기존에 @Secured 기반으로 작업된 사이트에서 필요에 따라 임시권한을 줄 수 있도록 설계된 것이 바로 RunAs 기능
    // 특정 상황에서 특정 권한을 가진 사람은 특별히 임시 권한을 주는 것
    // RunAsUserToken 을 만들어 기존에 있던 SecurityContextHolder 에 SecurityContext 와 교체하고,
    // finallyInvocation 에서 제거 후 원래의 SecurityContext 를 다시 넣어준다.
    // RunAsUserToken 은 그냥 ROLE_RUN_AS_... 권한이 추가된 토큰
    // GlobalMethodSecurityConfiguration 을 통해 설정 가능
    @Override
    protected RunAsManager runAsManager() {
        RunAsManagerImpl runas = new RunAsManagerImpl();
        runas.setKey("runas"); // RunAsUserToken 인증 해쉬 키값 지정
        return runas;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler(){
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
                CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, invocation);
                root.setPermissionEvaluator(getPermissionEvaluator());
                return root;
            }
        };
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());

        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        decisionVoters.add(new CustomVoter());

        return new AffirmativeBased(decisionVoters);
//        ConsensusBased committee = new ConsensusBased(decisionVoters);
//        committee.setAllowIfEqualGrantedDeniedDecisions(false);
//        return committee;
    }

}
