package com.ryanair.flights.interconnections.service;

import com.ryanair.flights.interconnections.domain.Interconnection;
import com.ryanair.flights.interconnections.domain.Leg;
import com.ryanair.flights.interconnections.domain.RouteDetail;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InterconnectionService {

    @Autowired
    RouteService routeService;

    @Autowired
    ScheduleService scheduleService;

    /**
     * This method control the flow of the service.
     * First will get the Direct Flights and then the Interconnected Flights
     * Will return empty list in case there is not data and exception in case there is not valid parameter.
     * @param departure IATA Code for Departure Airport.
     * @param arrival IATA Code for Arrival Airport
     * @param departureDateTime departure date time in ISO Format
     * @param arrivalDateTime arrival date time in ISO Format
     * @return List<Interconnection> List interconnections. stops 0: Direct flights and stops 1: Interconnected flights
     */
    public List<Interconnection> execute(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {

        List<Interconnection> interconnections = new ArrayList<>();

        if (departureDateTime.isBefore(arrivalDateTime)) {
            interconnections.add(getFlightsDirect(departure, arrival, departureDateTime, arrivalDateTime));
            interconnections.add(getFlightsOneStop(departure, arrival, departureDateTime, arrivalDateTime));
        }

        return interconnections;
    }

    /**
     * This method will return the direct flights.
     * It will call the method getRoutesScheduleDetail() from ScheduleService. Getting a Map with a list of RouteDetail
     * Will return empty list in case there is not data and exception in case there is not valid parameter.
     * @param departure IATA Code for Departure Airport
     * @param arrival IATA Code for Arrival Airport
     * @param localDepartureDateTime departure date time in ISO Format
     * @param localArrivalDateTime arrival date time in ISO Format
     * @return Interconnection with stop 0 and the list of the direct flights
     */
    @NonNull
    private Interconnection getFlightsDirect(String departure, String arrival, LocalDateTime localDepartureDateTime, LocalDateTime localArrivalDateTime) {

        Map<Map<String, String>, List<RouteDetail>> mapDirectFlight = scheduleService.getRoutesScheduleDetail(departure, arrival, localDepartureDateTime, localArrivalDateTime);
        List<Leg> directFlights = new ArrayList<>();
        Map<String, String> keyFlight = new HashMap<>();
        keyFlight.put(departure, arrival);

        if (!mapDirectFlight.isEmpty())
            directFlights = mapDirectFlight.get(keyFlight).get(0).getMapRoute().get(keyFlight);

        return new Interconnection(0, directFlights);
    }

    /**
     * This method will return the interconnected flights. Maximum one stop.
     * It will call RoutService to get the routes available and the connections between the two airports.
     * It will call the method getRoutesScheduleDetail() from ScheduleService. Getting a Map with a list of RouteDetail
     * Will return empty list in case there is not data and exception in case there is not valid parameter.
     * @param departure IATA Code for Departure Airport
     * @param arrival IATA Code for Arrival Airport
     * @param localDepartureDateTime departure date time in ISO Format
     * @param localArrivalDateTime arrival date time in ISO Format
     * @return Interconnection with stop 1 and the list of the interconnected flights
     */
    @NonNull
    private Interconnection getFlightsOneStop(String departure, String arrival, LocalDateTime localDepartureDateTime, LocalDateTime localArrivalDateTime) {

        List<String> connections = routeService.getAirportConnections(departure, arrival);
        Map<Map<String, String>, List<RouteDetail>> departureConnections = new HashMap<>();
        Map<Map<String, String>, List<RouteDetail>> arrivalConnections = new HashMap<>();
        List<Leg> validConnections = new ArrayList<>();

        for (String connection : connections) {
            departureConnections.putAll(scheduleService.getRoutesScheduleDetail(departure, connection, localDepartureDateTime, localArrivalDateTime));
            arrivalConnections.putAll(scheduleService.getRoutesScheduleDetail(connection, arrival, localDepartureDateTime, localArrivalDateTime));
        }

        //Key for first leg -> ("DEPARTURE -> CONNECTION")
        for (Map<String, String> keyFirstLeg : departureConnections.keySet()) {

            RouteDetail routeFirstLeg = departureConnections.get(keyFirstLeg).get(0);

            //Key for second leg -> ("CONNECTION -> ARRIVAL")
            Map<String, String> keySecondLeg = new HashMap<>();
            keySecondLeg.put(keyFirstLeg.values().toArray()[0].toString(), arrival);

            if (arrivalConnections.containsKey(keySecondLeg)) {

                RouteDetail routeSecondLeg = arrivalConnections.get(keySecondLeg).get(0);

                //All Connections works if Last Flight Leg 1 is two hours early than First Flight Leg 2
                if (routeFirstLeg.getMaxTimeFlightArrival().isBefore(routeSecondLeg.getMinTimeFlightDeparture().minusHours(2))) {
                    validConnections.addAll(routeFirstLeg.getMapRoute().get(keyFirstLeg));
                    validConnections.addAll(routeSecondLeg.getMapRoute().get(keySecondLeg));
                } else {
                    //Add every leg of Flight Leg 1 between the minimum time departure and maximum time departure of First Flight Leg 2
                    for (Leg flightTimeFirstLeg : routeFirstLeg.getMapRoute().get(keyFirstLeg)) {
                        if (flightTimeFirstLeg.getArrivalTime().isBefore(routeSecondLeg.getMinTimeFlightDeparture().minusHours(2)) ||
                                flightTimeFirstLeg.getArrivalTime().isBefore(routeSecondLeg.getMaxTimeFlightDeparture().minusHours(2))) {
                            validConnections.add(flightTimeFirstLeg);
                        }
                    }

                    //All Leg 2 works if First Flight Leg 2 is two hours later than First Flight Leg 1
                    if (routeFirstLeg.getMinTimeFlightArrival().isBefore(routeSecondLeg.getMinTimeFlightDeparture().minusHours(2))) {
                        validConnections.addAll(routeSecondLeg.getMapRoute().get(keySecondLeg));
                    } else {
                        //Every Flight of Leg 2 works if is later than the First Flight Leg 1 plus 2 hours
                        for (Leg flightTimeSecondLeg : routeSecondLeg.getMapRoute().get(keySecondLeg)) {
                            if (flightTimeSecondLeg.getDepartureTime().isAfter(routeFirstLeg.getMinTimeFlightArrival().plusHours(2))) {
                                validConnections.add(flightTimeSecondLeg);
                            }
                        }
                    }
                }
            }
        }

        return new Interconnection(1, validConnections);
    }
}