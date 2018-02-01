/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.fares.gateway;

import com.ryanair.hackathon.fares.model.FaresResponseDto;
import com.ryanair.hackathon.fares.model.OutboundDto;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RyanairFaresGateway {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${fares.api.path}")
    private String faresApi;

    @Value("${ryanair.apikey}")
    private String apikey;

    public Try<FaresResponseDto> getCheapestFaresByWeek(final String origin,
                                                   final String destination,
                                                   final String date) {
        return Try.of(() ->
                restTemplate.getForObject(getUrl(origin, destination, date), FaresResponseDto.class))
                .recover(error -> FaresResponseDto.builder().outboundDto(OutboundDto.builder().build()).build());
    }

    private String getUrl(final String origin,
                          final String destination,
                          final String date) {
        return faresApi  + origin + "/" + destination +"/"
                + "cheapestPerDay?outboundWeekOfDate=" + date + "&apikey=" + apikey;
    }
}
