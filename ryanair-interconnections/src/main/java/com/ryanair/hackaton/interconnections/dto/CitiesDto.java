package com.ryanair.hackaton.interconnections.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ryanair.hackaton.interconnections.model.City;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CitiesDto implements Serializable {

    private final City city;


    public static City of(String iataCode, String cityCode ) {
        return new City(iataCode, cityCode);
    }

    @JsonCreator
    public CitiesDto(@JsonProperty("iataCode") String iataCode,
                     @JsonProperty("cityCode") String cityCode ) {
        this.city = City.of(iataCode, cityCode);
    }

}
