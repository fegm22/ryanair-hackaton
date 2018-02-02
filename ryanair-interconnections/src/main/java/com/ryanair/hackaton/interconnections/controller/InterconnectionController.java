package com.ryanair.hackaton.interconnections.controller;

import com.ryanair.hackaton.interconnections.dto.FlightDto;
import com.ryanair.hackaton.interconnections.model.Airport;
import com.ryanair.hackaton.interconnections.model.Route;
import com.ryanair.hackaton.interconnections.service.CitiesServices;
import com.ryanair.hackaton.interconnections.service.RoutesService;
import com.ryanair.hackaton.interconnections.service.SearchFlightsService;
import com.ryanair.hackaton.interconnections.service.SearchRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
public class InterconnectionController {

    @Autowired
    private SearchFlightsService searchFlightsService;

    @Autowired
    private SearchRouteService searchRouteService;

    @Autowired
    private RoutesService routesService;

    @Autowired
    private CitiesServices citiesServices;


    @GetMapping(value = "/interconnections")
    public List<FlightDto>
    getInterconnections(@RequestParam(value = "departure") String departure,
                        @RequestParam(value = "arrival") String arrival,
                        @RequestParam(value = "departureDateTime") LocalDateTime departureDateTime,
                        @RequestParam(value = "arrivalDateTime") LocalDateTime arrivalDateTime) {

        LocalDateTime start = departureDateTime.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime end = arrivalDateTime.truncatedTo(ChronoUnit.MINUTES);

        return searchFlightsService.findFlights(new Airport(departure), new Airport(arrival), start, end, 1, 120,
                1440);
    }

    @GetMapping(value = "/available/routes")
    public Map<String, Set<String>> getAllAvailableRoutes() {

        return routesService.getAllAvailableRoutes();

    }

    @GetMapping(value = "/available/airports")
    public Map<String, String> getAllAvailableAirports() {

        return citiesServices.getAllAvailableAirports();
    }

    @GetMapping(value = "/routes")
    public List<List<Route>> findRoutesBetween(@RequestParam(value = "departure") Airport departure,
                                               @RequestParam(value = "arrival") Airport arrival,
                                               @RequestParam(value = "maxStops") Integer maxStops) {

        return searchRouteService.findRoutesBetween(departure, arrival, maxStops);
    }



}
