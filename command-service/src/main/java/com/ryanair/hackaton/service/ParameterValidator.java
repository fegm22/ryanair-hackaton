package com.ryanair.hackaton.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE;

/**
 * francisco . 2018
 **/
abstract class ParameterValidator {

    void validateDateAfterNow(Map<String, Object> parameters) {
        LocalDate localDate = Optional.ofNullable(parameters.get("date"))
                .map(Object::toString)
                .map(d -> LocalDate.parse(d, ISO_DATE))
                .orElseThrow(() -> new RuntimeException("no origin parameter"));

        if (localDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Date to find the fare is before now");
        }
    }

    void validateIATA(Map<String, Object> parameters, String parameter) {
        String iata = Optional.ofNullable(parameters.get(parameter))
                .map(Object::toString)
                .orElseThrow(() -> new RuntimeException("no origin parameter"));
        if (!iata.matches("[A-Z]{3}")) {
            throw new RuntimeException("iata code is not well formed");
        }
    }

    void validateFlightNumber(String flightNumber) {
        String flightNumber_ = Optional.ofNullable(flightNumber)
                .map(Object::toString)
                .orElseThrow(() -> new RuntimeException("no origin parameter"));
        if (!flightNumber_.matches("[0-9]{4,6}")) {
            throw new RuntimeException("Ryanair flight number not recognised");
        }
    }

    abstract void validate(Map<String, Object> parameters);
}
