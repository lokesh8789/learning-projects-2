package com.learn.repo.primary;

import com.learn.entity.primary.Candidate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CandidateRepo extends JpaRepository<Candidate,Long> {
    @Transactional
    @Modifying
    @Query(value = "insert into candidate(name,mobile,aadhaar) values(:name,:mobile,:aadhaar);",nativeQuery = true)
    int insertInDb(String name,String mobile,String aadhaar);
}
