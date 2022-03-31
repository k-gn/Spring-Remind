package com.sp.fc.web.student;

import com.sp.fc.web.teacher.Teacher;
import com.sp.fc.web.teacher.TeacherAuthenticationToken;
import com.sp.fc.web.test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if(studentDB.containsKey(token.getName())){
                return getAuthenticationToken(token.getName());
            }
            return null; // 해당 provider 에서 핸들링할 수 없을 경우엔 무조건 null 로 리턴해야 한다. 그래야 다음 provider 에게 넘어간다.
        }
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        if(studentDB.containsKey(token.getCredentials())){
            return getAuthenticationToken(token.getCredentials());
        }
        return null;
    }

    private StudentAuthenticationToken getAuthenticationToken(String id) {
        Student student = studentDB.get(id);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }

    public List<Student> myStudents(String teacherId){
        return studentDB.values().stream().filter(s->s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<test> testList = Arrays.asList(new test());
        authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        Student student = new Student("hong", "홍길동", authorities, "choi", testList);
        studentDB.put(student.getId(), student);

//        Set.of(
//                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi"),
//                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi"),
//                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi")
//        ).forEach(s->
//            studentDB.put(s.getId(), s)
//        );
    }
}
