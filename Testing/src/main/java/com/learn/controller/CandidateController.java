package com.learn.controller;

import com.learn.entity.primary.Project;
import com.learn.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    @Autowired
    CandidateService candidateService;
    @GetMapping("/")
    public ResponseEntity<Void> getCandidates() {
        candidateService.insertInDB();
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/find")
    public ResponseEntity<List<Project>> findAll() {
        return ResponseEntity.ok(candidateService.findAll());
    }
    @DeleteMapping("/")
    public void delete() {
        candidateService.delete();
    }
}
