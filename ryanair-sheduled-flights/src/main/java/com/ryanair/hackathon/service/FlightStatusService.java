package com.ryanair.hackathon.service;

import com.ryanair.hackathon.model.FlightInfo;

import java.util.List;

/**
 * francisco . 2018
 **/
public interface FlightStatusService {

    List<FlightInfo> getScheduledFlight(String flightNumber);
}
