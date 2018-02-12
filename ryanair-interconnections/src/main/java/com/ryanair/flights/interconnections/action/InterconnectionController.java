package com.ryanair.flights.interconnections.action;


import com.ryanair.flights.interconnections.domain.Interconnection;
import com.ryanair.flights.interconnections.service.CitiesServices;
import com.ryanair.flights.interconnections.service.InterconnectionService;
import com.ryanair.flights.interconnections.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class InterconnectionController {

    @Autowired
    private InterconnectionService interconnectionService;

    @Autowired
    private RouteService routesService;

    @Autowired
    private CitiesServices citiesServices;

    @RequestMapping(value = "/interconnections",
            method = RequestMethod.GET,
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Interconnection> getInterconnections(@RequestParam(value = "departure")
                                                      String departure,
                                              @RequestParam(value = "arrival")
                                                      String arrival,
                                              @RequestParam(value = "departureDateTime")
                                                      LocalDateTime departureDateTime,
                                              @RequestParam(value = "arrivalDateTime")
                                                      LocalDateTime arrivalDateTime) {

        LocalDateTime start = departureDateTime.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime end = arrivalDateTime.truncatedTo(ChronoUnit.MINUTES);

        return this.interconnectionService.execute(departure, arrival, start, end);
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
    public List<String> findRoutesBetween(@RequestParam(value = "departure") String departure,
                                               @RequestParam(value = "arrival") String arrival) {

        return routesService.getAirportConnections(departure, arrival);
    }

}
