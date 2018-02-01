/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.fares.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FareDto {
    private String day;
    private Price price;
    private Boolean soldOut;
    private Boolean unavailable;

    @JsonCreator
    public FareDto(
            @JsonProperty("day") String day,
            @JsonProperty("price") Price price,
            @JsonProperty("soldOut") Boolean soldOut,
            @JsonProperty("unavailable") Boolean unavailable) {
        this.day = day;
        this.price = price;
        this.soldOut = soldOut;
        this.unavailable = unavailable;
    }
}
