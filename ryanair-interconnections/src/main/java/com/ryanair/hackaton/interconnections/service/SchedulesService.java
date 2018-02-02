package com.ryanair.hackaton.interconnections.service;

import com.ryanair.hackaton.interconnections.dto.SchedulesDto;
import com.ryanair.hackaton.interconnections.integration.SchedulesApiClient;
import com.ryanair.hackaton.interconnections.model.Airport;
import com.ryanair.hackaton.interconnections.model.Flight;
import com.ryanair.hackaton.interconnections.model.Route;
import com.ryanair.hackaton.interconnections.model.Timetable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SchedulesService {

    private SchedulesApiClient schedulesApiClient;

    {
        schedulesApiClient = new SchedulesApiClient(
                new RestTemplate(),
                "https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{yearMonth}"
        );
    }

    @Cacheable("CACHE_SCHEDULES")
    public Optional<Timetable> getFlightsSchedules(Airport departure, Airport arrival, YearMonth yearMonth) {
        Route route = Route.of(departure, arrival);

        return schedulesApiClient
                .getSchedules(departure, arrival, yearMonth)
                .map(schedules -> getFlights(yearMonth, route, schedules))
                .map(flights -> new Timetable(route, yearMonth, flights));
    }

    private List<Flight> getFlights(YearMonth yearMonth, Route route, SchedulesDto schedules) {
        if (schedules.getMonth() != yearMonth.getMonthValue()) {
            throw new IllegalStateException(
                    "Wrong schedule received from Schedules API, expected " + yearMonth.getMonthValue() + " but got " + schedules.getMonth()
            );
        }

        return schedules.getDays()
                .stream()
                .flatMap(schedule -> getFlightStream(yearMonth, route, schedule))
                .sorted(Flight.FLIGHT_DEPARTURE_TIME_COMPARATOR)
                .collect(Collectors.toList());
    }

    private Stream<? extends Flight> getFlightStream(YearMonth yearMonth, Route route, SchedulesDto.Schedule day) {
        LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day.getDay());
        return day
                .getFlights()
                .stream()
                .map(c -> new Flight(c.getNumber(), route, c.getDepartureTime().atDate(date), c.getArrivalTime().atDate(date)));
    }
}
