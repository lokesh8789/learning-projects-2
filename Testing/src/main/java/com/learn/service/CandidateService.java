package com.learn.service;

import com.learn.entity.payroll.User;
import com.learn.entity.primary.Candidate;
import com.learn.entity.primary.Project;
import com.learn.repo.payroll.UserRepo;
import com.learn.repo.primary.CandidateRepo;
import com.learn.repo.primary.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {
    @Autowired
    CandidateRepo candidateRepo;
    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;
    public void insertInDB() {
        int b = candidateRepo.insertInDb("ankit", "5454545454", "989898989898");
        candidateRepo.save(Candidate.builder().name("Subham").aadhaar("787563287560").build());
        User lokesh = userRepo.save(User.builder().name("Lokesh").build());
        projectRepo.save(Project.builder().name("Software").userId(lokesh.getId()).build());
        System.out.println(b);
    }

    public List<Project> findAll(){
        return projectRepo.findAll();
    }

    public void delete() {
        candidateRepo.deleteById(1L);
        projectRepo.deleteById(1);
        userRepo.deleteById(1);
    }
}
