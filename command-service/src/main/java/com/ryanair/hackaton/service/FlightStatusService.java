package com.ryanair.hackaton.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ryanair.hackaton.client.FlightStatusClient;
import com.ryanair.hackaton.model.flightstatus.CommandResponse;
import com.ryanair.hackaton.model.flightstatus.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * francisco . 2018
 **/
@Service
public class FlightStatusService implements CommandService<FlightStatus> {

    public static final List<String> PARAMETERS_NAME = asList("flightNumber");

    private final ParameterValidator flightStatusValidator = new FlightStatusValidator();

    private final FlightStatusClient flightStatusClient;

    @Autowired
    public FlightStatusService(FlightStatusClient flightStatusClient) {
        this.flightStatusClient = flightStatusClient;
    }

    public CommandResponse processFallback(Map<String, Object> parameters) {
        return CommandResponse.builder()
                .header("Ryanair Flight Status Service")
                .message("Current service is not available")
                .answeredAt(LocalDateTime.now())
                .build();
    }


    @Override
    @HystrixCommand(fallbackMethod = "processFallback")
    public CommandResponse process(Map<String, Object> parameters) {
        flightStatusValidator.validate(parameters);

        List<FlightStatus> flightStatus =
                flightStatusClient.getFlightStatus(parameters.get(PARAMETERS_NAME.get(0)).toString());

        String message = flightStatus.stream().map(FlightStatus::message).collect(Collectors.joining("\n"));

        return CommandResponse.builder()
                .header("Ryanair Flight Status Service")
                .message(message)
                .answeredAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Class> match(String keyword) {
        if ("flight".equalsIgnoreCase(keyword)) {
            return Optional.of(FlightStatusService.class);
        }
        return Optional.empty();
    }

    private class FlightStatusValidator extends ParameterValidator {

        @Override
        void validate(Map<String, Object> parameters) {
            validateFlightNumber(parameters.get("flightNumber").toString());
        }
    }
}
