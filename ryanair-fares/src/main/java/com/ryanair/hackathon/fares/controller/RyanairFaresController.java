/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.fares.controller;

import com.ryanair.hackathon.fares.model.FareDto;
import com.ryanair.hackathon.fares.services.RyanairFaresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/fares")
public class RyanairFaresController {
    @Autowired
    private RyanairFaresService service;

    @GetMapping("/{origin}/{destination}/{date}")
    public List<FareDto> getFaresByDay(@PathVariable final String origin,
                                       @PathVariable final String destination,
                                       @PathVariable  final String date) {
        return service.getFaresByDate(origin, destination, date);
    }
}
