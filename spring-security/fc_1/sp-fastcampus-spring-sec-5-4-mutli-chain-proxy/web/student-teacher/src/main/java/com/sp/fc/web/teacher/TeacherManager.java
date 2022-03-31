package com.sp.fc.web.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if(teacherDB.containsKey(token.getName())){
                return getAuthenticationToken(token.getName());
            }
        }
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
//        Authentication.getCredentials()는 사용자 인증에 이용하는 정보
        if(teacherDB.containsKey(token.getCredentials())){
            return getAuthenticationToken(token.getCredentials());
        }
        // 해당 조건에 맞는게 없으면 null 을 리턴해줘야 한다.
        return null;
    }

    private TeacherAuthenticationToken getAuthenticationToken(String id) {
        Teacher teacher = teacherDB.get(id);
        return TeacherAuthenticationToken.builder()
                .principal(teacher)
                .details(teacher.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<GrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
        Teacher teacher = new Teacher("choi", "최선생", set);
        teacherDB.put(teacher.getId(), teacher);
//        Set.of(
//                new Teacher("choi", "최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
//        ).forEach(s->
//            teacherDB.put(s.getId(), s)
//        );
    }
}
