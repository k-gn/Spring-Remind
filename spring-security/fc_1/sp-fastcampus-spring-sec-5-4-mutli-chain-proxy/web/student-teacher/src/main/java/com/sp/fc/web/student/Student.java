package com.sp.fc.web.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sp.fc.web.test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    private String id;
    private String username;

//    @JsonIgnore
    private Set<GrantedAuthority> role;

    private String teacherId;

    private List<test> testList;

}
