package org.ryanairbot.service;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.json.JSONObject;
import org.ryanairbot.dto.FlightDto;
import org.ryanairbot.model.Airport;
import org.ryanairbot.model.Flight;
import org.ryanairbot.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * francisco . 2018
 **/
@Service
public class InternalRyanairService {

    private final SearchRouteService searchRouteService;
    private final SearchFlightsService searchFlightsService;
    private final RoutesService routesService;
    private final CitiesServices citiesServices;

    @Value("${ryanair.commandservice.url}")
    private String commandServiceUrl;

    @Autowired
    public InternalRyanairService(SearchRouteService searchRouteService, SearchFlightsService searchFlightsService,
                                  RoutesService routesService, CitiesServices citiesServices) {

        this.searchRouteService = searchRouteService;
        this.searchFlightsService = searchFlightsService;
        this.routesService = routesService;
        this.citiesServices = citiesServices;
    }

    public String processMessage(String message) {

        DirectedGraph<Airport, DefaultEdge> routes = routesService.getAllAvailableRoutes();

        Map<String, String> citiesMap = citiesServices.getAllAvailableAirports();
        Map<Integer, String> citiesQuery = getCitiesQuery(message, routes, citiesMap);

        if (citiesQuery.size() == 2) {
            Airport departure = new Airport(citiesQuery.get(0));
            Airport arrival = new Airport(citiesQuery.get(1));

            String instruction = getInstruction(message);
            return getResultMessage(departure, arrival, instruction, citiesMap);

        } else {
            return helpMessage();
        }
    }


    private Map<Integer, String> getCitiesQuery(String query,
                                                DirectedGraph<Airport, DefaultEdge> routes,
                                                Map<String, String> citiesMap) {
        String[] items = query.split(" ");
        List<String> queryWords = Arrays.asList(items);

        int contador = 0;
        Map<Integer, String> cities = new HashMap<>();
        for (String word : queryWords) {
            Airport airport = new Airport(word);
            if (routes.containsVertex(airport)) {
                cities.put(contador++, word.toUpperCase());
            } else if (citiesMap.containsKey(word.toUpperCase())) {
                //cities.put(contador++, getKeysByValue(citiesMap, word.toUpperCase()) );
                cities.put(contador++, citiesMap.get(word.toUpperCase()));
            }
        }
        return cities;
    }


    private String getInstruction(String query) {
        Set<String> setInstructions = new HashSet<>();
        setInstructions.add("CONNECTIONS");
        setInstructions.add("FLIGHTS");
        setInstructions.add("FLIGHT");

        String[] items = query.split(" ");
        List<String> queryWords = Arrays.asList(items);

        String instruction = "";
        for (String word : queryWords) {
            if (setInstructions.contains(word.toUpperCase())) {
                instruction = word.toUpperCase();
            }
        }
        return instruction;
    }



    private String getResultMessage(Airport departure, Airport arrival, String instruction, Map<String, String> citiesMap) {
        String result = "";
        if (instruction.isEmpty()) {
            result = "Sorry, I didn't understand your question. But here are the direct flights of the week \n\n";

            result = result + getFlightsDirect(departure, arrival,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusWeeks(1),
                    citiesMap);

        } else {
            if (instruction.toUpperCase().equals("CONNECTIONS")) {
                result = getConnectionsResponse(departure, arrival, citiesMap);
            }
            if (instruction.toUpperCase().equals("FLIGHTS")) {
                result = getFlightsDirect(departure, arrival,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusWeeks(1),
                        citiesMap);
            }
        }
        return result;
    }

    private String getConnectionsResponse(Airport departure, Airport arrival, Map<String, String> cities) {
        String result;

        List<List<Route>> connections = searchRouteService.findRoutesBetween(departure, arrival, 0);

        result = "This are all the connections between " + cities.get(departure.getIataCode()) + " and " + cities.get(arrival.getIataCode()) + "\n\n";

        List<Route> routingList = connections.get(0);

        if (routingList.size() > 1) {
            for (Route citiConnect : routingList) {
                result = result + cities.get(citiConnect.getFrom().getIataCode()).replace("_", " ") + "\n";
            }
        } else {
            result = "Good news!!! You can travel directly from " +
                    cities.get(departure.getIataCode())
                    + " to " +
                    cities.get(arrival.getIataCode()) + "\n\n";
            ;
        }

        return result;
    }

    private String getFlightsDirect(Airport departure, Airport arrival,
                                    LocalDateTime localDepartureDateTime,
                                    LocalDateTime localArrivalDateTime,
                                    Map<String, String> cities) {


        List<FlightDto> directFlights = searchFlightsService.findFlights(departure, arrival, localDepartureDateTime, localArrivalDateTime, 0, 0, 0);

        String result = "";
        if (!directFlights.isEmpty()) {
            result = "The flights from " + cities.get(departure.getIataCode()) + " to " + cities.get(arrival
                    .getIataCode()) + " for this week are :\n\n";
            result = result + "Number      Departure                      Arrival \n";

            for (FlightDto leg : directFlights) {
                result = result +
                        leg.getLegs().stream().map(Flight::getNumber).collect(Collectors.toList()).get(0).toString() + "            " +
                        leg.getLegs().stream().map(Flight::getDepartureTime).collect(Collectors.toList()).get(0).toString() + "        " +
                        leg.getLegs().stream().map(Flight::getArrivalTime).collect(Collectors.toList()).get(0).toString() + "\n";
            }
        }

        return result;
    }

    private String helpMessage() {
        String message = "Sorry but I couldn't understand your question. \n\n";

        message = message + "Try something like...\n\n";
        message = message + "- Give me the CONNECTIONS between MAD and WRO\n";
        message = message + "- Give me FLIGHTS between Madrid and Dublin\n\n";
        return message;
    }

    private String getGETRequest(String query) {
        final String uri = commandServiceUrl;
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> routes = restTemplate.exchange(uri,
                HttpMethod.GET, null,
                responseType);

        String responseString = routes.getBody();
        JSONObject jsonObject = new JSONObject(responseString);

        String message = jsonObject.getString("header") + "\n\n" + jsonObject.getString("message");

        return message;

    }


}
