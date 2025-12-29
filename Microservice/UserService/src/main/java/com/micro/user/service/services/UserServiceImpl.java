package com.micro.user.service.services;

import com.micro.user.service.entities.User;
import com.micro.user.service.exceptions.ResourceNotFoundException;
import com.micro.user.service.external.HotelService;
import com.micro.user.service.model.Hotel;
import com.micro.user.service.model.Rating;
import com.micro.user.service.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HotelService hotelService;
    @Override
    public User saveUser(User user) {
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found with Id: " + userId));
        Rating[] arr = restTemplate.getForObject("http://rating-service/ratings/users/"+user.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.stream(arr).toList();
        log.info("{}", ratings);
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel-service to get the hotel
            //set hotel to rating
            //return rating

            //-> RestTemplate Way
            /*ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://hotel-service/hotels/"+rating.getHotelId(), Hotel.class);
            rating.setHotel(forEntity.getBody());*/

            //-> FeignClient Way
            rating.setHotel(hotelService.getHotel(rating.getHotelId()));
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }
}
