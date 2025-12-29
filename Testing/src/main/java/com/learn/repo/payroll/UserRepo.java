package com.learn.repo.payroll;

import com.learn.entity.payroll.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
}
