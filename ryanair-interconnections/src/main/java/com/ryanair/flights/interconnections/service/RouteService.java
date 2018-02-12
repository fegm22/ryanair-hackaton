package com.ryanair.flights.interconnections.service;

import com.ryanair.flights.interconnections.domain.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouteService {

    /**
     * This method will return the routes provided by the Ryanair API.
     *
     * @return List<Route> with stop 1 and the list of the interconnected flights
     */
    public List<Route> getRoutes() {

        final String uri = "https://api.ryanair.com/core/3/routes/";
        ParameterizedTypeReference<List<Route>> responseType = new ParameterizedTypeReference<List<Route>>() {
        };

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Route>> routes = restTemplate.exchange(uri,
                HttpMethod.GET, null,
                responseType);

        List<Route> listRoutes = routes.getBody();

        return listRoutes;

    }

    /**
     * This method will return a list of all airports who connect departure with arrival
     * It use the list of routes returned by the Ryanair API
     *
     * @param departure  IATA Code for Departure Airport
     * @param arrival    IATA Code for Arrival Airport
     * @return list of IATA code connections
     */
    public List<String> getAirportConnections(String departure, String arrival) {
        Map<String, Set<String>> mapRoute = getAllAvailableRoutes();

        Set<String> arrivalFromOrigin = mapRoute.entrySet().stream()
                .filter(map -> departure.equals(map.getKey()))
                .map(map -> map.getValue())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        List<String> airportsConnection = new ArrayList<>();

        arrivalFromOrigin.stream().forEach(connection -> {
                    Set<String> destiny = mapRoute.get(connection);
                    if (destiny.contains(arrival)) {
                        airportsConnection.add(connection);
                    }
                }
        );

        return airportsConnection;
    }

    /**
     * This method will return a map of all airports and his connections
     *
     * @return a map of every Airport and his connections in a list.
     */
    public Map<String, Set<String>> getAllAvailableRoutes() {

        List<Route> listRoutes = getRoutes();

        Map<String, Set<String>> mapRoute = new HashMap<>();

        listRoutes.stream().forEach(route -> addRoute(mapRoute, route));

        return mapRoute;
    }

    private void addRoute(Map<String, Set<String>> mapRoute, Route route) {
        String airportFrom = route.getAirportFrom();
        String airportTo = route.getAirportTo();

        mapRoute.computeIfAbsent(airportFrom, i -> new HashSet<>()).add(airportTo);
    }

}
