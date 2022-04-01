package com.sp.fc.paper.service;

import com.sp.fc.paper.domain.Problem;
import com.sp.fc.paper.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    // 문제 등록
    public Problem save(Problem problem) {
        if(problem.getProblemId() == null){
            problem.setCreated(LocalDateTime.now());
        }
        problem.setUpdated(LocalDateTime.now());
        return problemRepository.save(problem);
    }

    // 문제 조회
    public Optional<Problem> findProblem(Long problemId){
        return problemRepository.findById(problemId);
    }

    // 한 템플릿 시험지의 모든 문제 조회
    public List<Problem> listProblems(long paperTemplateId){
        return problemRepository.findAllByPaperTemplateIdOrderByIndexNumAsc(paperTemplateId);
    }

    // 문제 삭제
    public void delete(Problem problem) {
        problemRepository.delete(problem);
    }

    // 문제 수정
    public void updateProblem(long problemId, String content, String answer) {
        problemRepository.updateProblem(problemId, content, answer, LocalDateTime.now());
    }

}
