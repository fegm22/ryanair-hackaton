package com.ryanair.hackaton.client;

import com.ryanair.hackaton.model.flightstatus.FlightStatus;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "flightstatus", url = "${flightstatus.feign.locationOfServers}")
public interface FlightStatusClient {

    @RequestMapping(method = GET, path = "/flightstatus/{flightNumber}")
    List<FlightStatus> getFlightStatus(@PathVariable("flightNumber") String flightNumber);

}


