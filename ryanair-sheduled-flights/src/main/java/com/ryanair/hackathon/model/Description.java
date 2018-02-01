package com.ryanair.hackathon.model;

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
public class Description {

    private String code;
    private String message;

    public Description(@JsonProperty("code") String code,
                       @JsonProperty("message") String message) {
        this.code = code;
        this.message = message;
    }
}
