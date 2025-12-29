package com.micro.user.service.controller;

import com.micro.user.service.entities.User;
import com.micro.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    int retryCount = 1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    //@Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter",fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId){
        log.info("Retry Count: {}",retryCount);
        retryCount++;
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    //creating fallBack method
    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex) {
        log.info("Fallback method called because service is down: {}",ex.getMessage());
        User user = User.builder()
                .email("dummy@mail.com")
                .name("dummy")
                .about("nice dummy")
                .userId("123456")
                .build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}
