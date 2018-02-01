package com.ryanair.hackaton.model.flightstatus;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryanair.hackaton.model.MessageResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightStatus implements MessageResponse{

    private String number;

    private Airport departureAirport;

    private Airport arrivalAirport;

    private String departureTime;

    private String arrivalTime;

    @JsonCreator
    public FlightStatus(
            @JsonProperty("number") String number,
            @JsonProperty("departureAirport") Airport departureAirport,
            @JsonProperty("arrivalAirport") Airport arrivalAirport,
            @JsonProperty("departureTime") String departureTime,
            @JsonProperty("arrivalTime") String arrivalTime) {

        this.number = number;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }


    @Override
    public String message() {
        return new StringBuffer().append("The flight number ")
                .append(number).append(" from ").append(departureAirport.getName())
                .append(" to ").append( arrivalAirport.getName() )
                .append(" is scheduled at ").append(departureTime)
                .append(" ,arriving at ").append(arrivalTime)
                .append(" at it is on time")
                .toString();
    }
}


