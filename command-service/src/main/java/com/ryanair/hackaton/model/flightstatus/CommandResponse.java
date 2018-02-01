package com.ryanair.hackaton.model.flightstatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * francisco . 2018
 **/
@Data
@Builder
@AllArgsConstructor
public class CommandResponse {

    private String message;
    private String header;
    private String image;
    private LocalDateTime answeredAt;

}
