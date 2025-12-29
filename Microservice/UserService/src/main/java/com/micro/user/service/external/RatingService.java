package com.micro.user.service.external;

import com.micro.user.service.model.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "rating-service")
public interface RatingService {

    @PostMapping("/ratings/")
    Rating createRating(Rating values);
}
