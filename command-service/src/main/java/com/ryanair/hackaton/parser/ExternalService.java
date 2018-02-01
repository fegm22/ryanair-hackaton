package com.ryanair.hackaton.parser;

import com.ryanair.hackaton.service.FareFinderService;
import com.ryanair.hackaton.service.FlightStatusService;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * francisco . 2018
 **/
enum ExternalService {

    FLIGH_STATUS("flight", FlightStatusService.PARAMETERS_NAME),
    FARE_FINDER("fare", FareFinderService.PARAMETERS_NAMES);

    private final String name;

    private final List<String> parameters;

    ExternalService(String name, List<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public static ExternalService findByName(String name) {
        return Arrays.stream(ExternalService.values()).filter(e -> e.getName().equals(name)).findAny().orElse(null);
    }
}
