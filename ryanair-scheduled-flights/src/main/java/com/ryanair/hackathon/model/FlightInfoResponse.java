package com.ryanair.hackathon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * francisco . 2018
 **/
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightInfoResponse {

    private final Integer total;

    private final List<FlightInfo> flightInfo;

    @JsonCreator
    public FlightInfoResponse(@JsonProperty("total") Integer total,
                              @JsonProperty("flightInfo") List<FlightInfo> flightInfo) {
        this.total = total;
        this.flightInfo = flightInfo;
    }
}
