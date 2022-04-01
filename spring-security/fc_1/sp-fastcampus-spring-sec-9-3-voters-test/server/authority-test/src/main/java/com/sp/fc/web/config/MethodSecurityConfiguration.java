package com.sp.fc.web.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.annotation.Jsr250Voter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

// GlobalMethodSecurityConfiguration - 메소드 권한을 설정 ( Method level에 Security를 설정 )
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    // hasPermission을 사용하려면 permission을 evaluation할 수 있는 클래스를 만들고 GlobalMethodSecurity 설정에 연결 해야한다.
    @Autowired
    private CustomPermissionEvaluator permissionEvaluator;

    // 핸들러를 오버라이딩 해서 CustomMethodSecurityExpressionRoot 와 CustomPermissionEvaluator 를 적용
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        // MethodSecurityExpressionHandler 구현 객체 = DefaultMethodSecurityExpressionHandler
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler(){
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
                CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, invocation);
                root.setPermissionEvaluator(getPermissionEvaluator());
                return root;
            }
        };
        // handler 에 permissionEvaluator 를 넣고 root 에 전달
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
//        decisionVoters.add(new CustomVoter());

        return new AffirmativeBased(decisionVoters);
//        ConsensusBased committee = new ConsensusBased(decisionVoters);
//        committee.setAllowIfEqualGrantedDeniedDecisions(false);
//        return committee;
    }

}
