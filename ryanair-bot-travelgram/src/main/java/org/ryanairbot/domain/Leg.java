package org.ryanairbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Leg {

    private static final long serialVersionUID = 1L;

    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDateTime;
    private String arrivalDateTime;
    @JsonIgnore
    private LocalDateTime departureTime;
    @JsonIgnore
    private LocalDateTime arrivalTime;

    public Leg() {

    }

    public Leg(String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureDateTime = departureTime.toString();
        this.arrivalDateTime = arrivalTime.toString();
    }
}
