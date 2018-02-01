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

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaresResponseDto {
    private final OutboundDto outboundDto;

    @JsonCreator
    public FaresResponseDto(@JsonProperty("outbound") final OutboundDto outbound) {
        this.outboundDto = outbound;
    }
}
