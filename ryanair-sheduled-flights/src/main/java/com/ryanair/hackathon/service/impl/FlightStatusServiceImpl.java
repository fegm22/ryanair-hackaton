package com.ryanair.hackathon.service.impl;

import com.ryanair.hackathon.client.FlightStatsClient;
import com.ryanair.hackathon.model.FlightInfo;
import com.ryanair.hackathon.service.FlightStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * francisco . 2018
 **/
@Component
public class FlightStatusServiceImpl implements FlightStatusService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final FlightStatsClient flightStatsClient;

    @Value("${flightstatus.apiKey}")
    private String apiKey;

    public FlightStatusServiceImpl(FlightStatsClient flightStatsClient) {
        this.flightStatsClient = flightStatsClient;
    }

    @Override
    public List<FlightInfo> getScheduledFlight(String flightNumber) {
        LOGGER.info("finding scheduled times for the flight status");
        return this.flightStatsClient.getScheduledFlights(flightNumber, apiKey).getFlightInfo();
    }
}
