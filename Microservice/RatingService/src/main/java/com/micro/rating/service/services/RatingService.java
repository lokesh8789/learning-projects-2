package com.micro.rating.service.services;

import com.micro.rating.service.entities.Rating;

import java.util.List;

public interface RatingService {
    Rating create(Rating rating);
    List<Rating> getAll();
    List<Rating> getAllByUserId(String userId);
    List<Rating> getRatingByHotelId(String hotelId);
}
