package com.ryanair.hackathon.controller;

import com.ryanair.hackathon.model.FlightInfo;
import com.ryanair.hackathon.service.FlightStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * francisco . 2018
 **/
@RestController
@RequestMapping("/flightstatus")
public class FlightStatusController {

    private final FlightStatusService flightStatusService;

    public FlightStatusController(FlightStatusService flightStatusService) {
        this.flightStatusService = flightStatusService;
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<List<FlightInfo>> getScheduledFlight(@PathVariable("flightNumber") String flightNumber) {

        return ResponseEntity.ok(flightStatusService.getScheduledFlight(flightNumber));
    }

}
