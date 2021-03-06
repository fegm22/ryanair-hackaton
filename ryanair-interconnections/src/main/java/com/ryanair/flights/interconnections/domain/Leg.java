package com.ryanair.flights.interconnections.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Leg {

    private static final long serialVersionUID = 1L;

    private final String flightNumber;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String departureDateTime;
    private final String arrivalDateTime;
    @JsonIgnore
    private final LocalDateTime departureTime;
    @JsonIgnore
    private final LocalDateTime arrivalTime;

    public Leg(String flightNumber, String departureAirport, String arrivalAirport, LocalDateTime departureTime,
               LocalDateTime arrivalTime) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureDateTime = departureTime.toString();
        this.arrivalDateTime = arrivalTime.toString();
    }
}
