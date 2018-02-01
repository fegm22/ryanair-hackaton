/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.fares.services;

import com.ryanair.hackathon.fares.gateway.RyanairFaresGateway;
import com.ryanair.hackathon.fares.model.FareDto;
import com.ryanair.hackathon.fares.model.FaresResponseDto;
import com.ryanair.hackathon.fares.model.OutboundDto;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RyanairFaresService {
    @Value("${ryanair.apikey}")
    private String apikey;

    @Autowired
    private RyanairFaresGateway gateway;

    public List<FareDto> getFaresByDate(final String origin,
                                        final String destination,
                                        final String date) {
        return gateway.getCheapestFaresByWeek(origin, destination, date)
                .map(r -> r.getOutboundDto().getFares())
                .get()
                .stream().filter(p -> !p.getUnavailable() && date.equals(p.getDay()))
                .collect(Collectors.toList());
    }
}
