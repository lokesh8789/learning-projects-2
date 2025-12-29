package com.micro.hotel.service.controllers;

import com.micro.hotel.service.entities.Hotel;
import com.micro.hotel.service.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    @Autowired
    HotelService hotelService;

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/")
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        return new ResponseEntity<>(hotelService.create(hotel), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_internal')")
    @GetMapping("/")
    public ResponseEntity<List<Hotel>> getAll(){
        return new ResponseEntity<>(hotelService.getAll(),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin')")
    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> get(@PathVariable String hotelId) {
        return new ResponseEntity<>(hotelService.get(hotelId),HttpStatus.OK);
    }
}
