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
public class Airport {

    private final String iata;
    private final String name;

    @JsonCreator
    public Airport(@JsonProperty("iata") String iata,
                   @JsonProperty("name") String name) {
        this.iata = iata;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", iata='" + iata + '\'' +
                '}';
    }
}
