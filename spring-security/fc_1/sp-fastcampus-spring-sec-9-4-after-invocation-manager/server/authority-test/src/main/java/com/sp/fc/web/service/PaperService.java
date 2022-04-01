package com.sp.fc.web.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaperService implements InitializingBean {

    private HashMap<Long, Paper> paperDB = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setPaper(Paper paper){
        paperDB.put(paper.getPaperId(), paper);
    }

    // 보통 이런 권한 검사는 Controlelr 단에서 하는게 더 자연스럽다.
    @PostFilter("notPrepareSate(filterObject)")
    public List<Paper> getMyPapers(String username) {
        System.out.println("getMyPapers -----------------------------");
//        return paperDB.values().stream().collect(Collectors.toList());
        return paperDB.values().stream().filter(
                paper -> paper.getStudentIds().contains(username)
        ).collect(Collectors.toList());
    }

    // 일반적으로 리턴이 하나만 갈 경우 PostAuthorize를 사용하고 List로 내려갈 때 PostFilter 를 사용한다.

    public Paper getPaper(Long paperId) {
        return paperDB.get(paperId);
    }
}
