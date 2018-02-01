/*
 * Copyright (c) 2018 Ryanair Ltd. All rights reserved.
 */
package com.ryanair.hackaton.model.farefinder;


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
public class Price {
    private Double value;
    private String valueMainUnit;
    private String valueFractionalUnit;
    private String currencyCode;
    private String currencySymbol;

    @JsonCreator
    public Price(
            @JsonProperty("value") Double value,
            @JsonProperty("valueMainUnit") String valueMainUnit,
            @JsonProperty("valueFractionalUnit") String valueFractionalUnit,
            @JsonProperty("currencyCode") String currencyCode,
            @JsonProperty("currencySymbol") String currencySymbol) {
        this.value = value;
        this.valueMainUnit = valueMainUnit;
        this.valueFractionalUnit = valueFractionalUnit;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
    }
}
