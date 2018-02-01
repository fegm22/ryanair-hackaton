package com.ryanair.hackaton.model.flightstatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * francisco . 2018
 **/
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartureAirport {

    private final String gate;
    private final String terminal;

    @JsonCreator
    public DepartureAirport(@JsonProperty("gate") String gate,
                            @JsonProperty("terminal") String terminal) {
        this.gate = gate;
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return "{" +
                "gate='" + gate + '\'' +
                ", terminal='" + terminal + '\'' +
                '}';
    }
}
