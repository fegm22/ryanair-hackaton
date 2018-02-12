package com.ryanair.hackathon.client;

import com.ryanair.hackathon.model.FlightInfoResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * francisco . 2018
 **/
@FeignClient(name = "flightstatus", url = "${flightstatus.feign.listOfServers}")
public interface FlightStatsClient {

    @GetMapping(path = "/pub/v1/flightinfo/2/flights?flight={flightNumber}&apikey={apiKey}")
    FlightInfoResponse getScheduledFlights(
            @PathVariable(value = "flightNumber") String flightNumber,
            @PathVariable(value = "apiKey") String appKey);
}
