package com.ryanair.hackathon.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightInfo {

    private String number;

    private Airport departureAirport;

    private Airport arrivalAirport;

    private String departureTime;

    private String arrivalTime;

    private Description description;

    @JsonCreator
    public FlightInfo(
            @JsonProperty("number") String number,
            @JsonProperty("departureAirport") Airport departureAirport,
            @JsonProperty("arrivalAirport") Airport arrivalAirport,
            @JsonProperty("departureTime") String departureTime,
            @JsonProperty("arrivalTime") String arrivalTime,
            @JsonProperty("description") Description description) {

        this.number = number;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                "number='" + number + '\'' +
                ", departureAirport=" + departureAirport +
                ", arrivalAirport=" + arrivalAirport +
                '}';
    }
}


