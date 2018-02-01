/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackathon.fares.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutboundDto {
    private final List<FareDto> fares;

    @JsonCreator
    public OutboundDto(@JsonProperty("fares") List<FareDto> fares) {
        this.fares = fares;
    }
}
