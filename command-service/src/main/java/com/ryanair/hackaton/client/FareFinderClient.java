package com.ryanair.hackaton.client;

import com.ryanair.hackaton.model.farefinder.FareDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * francisco . 2018
 **/
@FeignClient(name = "RYANAIR-FARE")
public interface FareFinderClient {

    @GetMapping("/v1/fares/{origin}/{destination}/{date}")
    List<FareDto> getFaresByDay(@PathVariable("origin") final String origin,
                                @PathVariable("destination") final String destination,
                                @PathVariable("date") final String date);
}
