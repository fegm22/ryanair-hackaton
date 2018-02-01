package com.ryanair.hackaton.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.ryanair.hackaton.client.FareFinderClient;
import com.ryanair.hackaton.model.farefinder.FareDto;
import com.ryanair.hackaton.model.flightstatus.CommandResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * francisco . 2018
 **/
@Service
public class FareFinderService implements CommandService<FareDto> {

    public static final List<String> PARAMETERS_NAMES = Arrays.asList("origin", "destination", "date");

    private final FareFinderClient fareFinderClient;

    private final FareFinderValidator fareFinderValidator = new FareFinderValidator();

    public FareFinderService(FareFinderClient fareFinderClient) {
        this.fareFinderClient = fareFinderClient;
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
        fareFinderValidator.validate(parameters);

        List<FareDto> faresByDay = fareFinderClient.getFaresByDay(
                parameters.get(PARAMETERS_NAMES.get(0)).toString(),
                parameters.get(PARAMETERS_NAMES.get(1)).toString(),
                parameters.get(PARAMETERS_NAMES.get(2)).toString()
        );

        String message = faresByDay.stream()
                .map(FareDto::message)
                .map(m -> m.replace("<origin>", parameters.get(PARAMETERS_NAMES.get(0)).toString())
                        .replace("<destination>", parameters.get(PARAMETERS_NAMES.get(1)).toString())
                )
                .collect(Collectors.joining("\n"));

        return CommandResponse.builder()
                .header("Ryanair Fares Finder")
                .message(message)
                .answeredAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Class> match(String keyword) {
        if ("fare".equalsIgnoreCase(keyword)) {
            return Optional.of(FlightStatusService.class);
        }
        return Optional.empty();
    }

    private class FareFinderValidator extends ParameterValidator {

        public void validate(Map<String, Object> parameters) {

            validateIATA(parameters, "origin");

            validateIATA(parameters, "destination");

            validateDateAfterNow(parameters);
        }

    }
}
