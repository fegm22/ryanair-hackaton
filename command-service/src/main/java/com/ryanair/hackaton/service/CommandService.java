package com.ryanair.hackaton.service;

import com.ryanair.hackaton.model.flightstatus.CommandResponse;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * francisco . 2018
 **/
@FunctionalInterface
public interface CommandService<T> {

    CommandResponse process(Map<String, Object> parameters);

    default Optional<Class> match(String keyword) {
        return Optional.empty();
    }

}
