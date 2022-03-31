package com.sp.fc.web.controller;

import com.sp.fc.web.student.StudentManager;
import com.sp.fc.web.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentManager studentManager;


    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")
    @GetMapping("/main")
    // @authenticationprincipal : UserDetailsService에서 Return한 객체 를 파라메터로 직접 받아 사용
    // getPrincipal() 로 리턴 받을 수 있는 객체를 바로 주입받을 수가 있음
    public String main(@AuthenticationPrincipal Teacher teacher, Model model){
        model.addAttribute("studentList", studentManager.myStudents(teacher.getId()));
        return "TeacherMain";
    }


}
