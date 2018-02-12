package org.ryanairbot.client;

import org.ryanairbot.domain.FlightDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@FeignClient("RYANAIR-INTERCONNECTIONS")
public interface InterconnectionsClient {

    @GetMapping("/interconnections")
    List<FlightDto> getInterconnections(@RequestParam(value = "departure") final String departure,
                                        @RequestParam(value = "arrival") final String arrival,
                                        @RequestParam(value = "departureDateTime") final LocalDateTime departureDateTime,
                                        @RequestParam(value = "arrivalDateTime") final LocalDateTime arrivalDateTime);

    @GetMapping(value = "/available/routes")
    Map<String, Set<String>> getAllAvailableRoutes();


    @GetMapping(value = "/available/airports")
    Map<String, String> getAllAvailableAirports();

    @GetMapping(value = "/routes/")
    List<String> findRoutesBetween(@RequestParam(value = "departure") final String departure,
                                        @RequestParam(value = "arrival") final String arrival);

}
