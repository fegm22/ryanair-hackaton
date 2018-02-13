package org.ryanairbot.service;

import org.ryanairbot.client.CommandClient;
import org.ryanairbot.client.InterconnectionsClient;
import org.ryanairbot.domain.Interconnection;
import org.ryanairbot.domain.Leg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RyanairService {

    private final InterconnectionsClient interconnectionsClient;
    private final CommandClient commandClient;

    @Autowired
    RyanairService(InterconnectionsClient interconnectionsClient, CommandClient commandClient) {
        this.interconnectionsClient = interconnectionsClient;
        this.commandClient = commandClient;

    }

    /**
     * This method will return the response base on the questions ask in telegram
     *
     * @param query Text from Telegram
     * @return Return the answer
     */
    public String processMessage(String query) throws RuntimeException {

        if (query.toUpperCase().contains("API")) {
            return commandClient.processMessage(query);
        } else {
            return artificialIntelligenceProcess(query);
        }
    }

    private String artificialIntelligenceProcess(String message) {

        Map<String, String> citiesMap = interconnectionsClient.getAllAvailableAirports();
        Map<String, Set<String>> routes = interconnectionsClient.getAllAvailableRoutes();
        Map<Integer, String> citiesQuery = getCitiesQuery(message, routes, citiesMap);

        if (citiesQuery.size() == 2) {
            String departure = citiesQuery.get(0);
            String arrival = citiesQuery.get(1);

            String instruction = getInstruction(message);
            return getResultMessage(departure, arrival, instruction, citiesMap);

        } else {
            return helpMessage();
        }
    }

    private Map<Integer, String> getCitiesQuery(String query,
                                                Map<String, Set<String>> routes,
                                                Map<String, String> citiesMap) {
        String[] items = query.split(" ");
        List<String> queryWords = Arrays.asList(items);

        int index = 0;
        Map<Integer, String> cities = new HashMap<>();
        for (String word : queryWords) {
            if (routes.containsKey(word.toUpperCase())) {
                cities.put(index++, word.toUpperCase());
            } else if (citiesMap.containsValue(word.toUpperCase())) {
                cities.put(index++, getKeysByValue(citiesMap, word.toUpperCase()));
            }
        }
        return cities;
    }

    public static String getKeysByValue(Map<String, String> map, String value) {
        return map.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()).stream().findFirst().get();
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

    private String getResultMessage(String departure, String arrival, String instruction, Map<String, String> citiesMap) {
        String result = "";
        if (instruction.isEmpty()) {
            result = "Sorry, I didn't understand your question. But here are the direct flights for the next day" +
                    " \n\n";

            result = result + getFlightsDirect(departure, arrival,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    citiesMap);

        } else {
            if (instruction.toUpperCase().equals("CONNECTIONS")) {
                result = getConnectionsResponse(departure, arrival, citiesMap);
            }
            if (instruction.toUpperCase().equals("FLIGHTS")) {
                result = getFlightsDirect(departure, arrival,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        citiesMap);
            }
        }
        return result;
    }

    private String getConnectionsResponse(String departure, String arrival, Map<String, String> cities) {
        String result = "This are all the connections between " + cities.get(departure) + " and " + cities.get
                (arrival) + "\n\n";

        List<String> connections = interconnectionsClient.findRoutesBetween(departure, arrival);

        if (connections.isEmpty()) {
            return "I'm sorry there is not connections between " + cities.get(departure) + " and " + cities.get
                    (arrival) + "\n\n";
        } else {

            if (connections.size() > 1) {
                for (String citiConnect : connections) {
                    result = result + cities.get(citiConnect).replace("_", " ") + "\n";
                }
            } else {
                result = "Good news!!! You can travel directly from " +
                        cities.get(departure)
                        + " to " +
                        cities.get(arrival) + "\n\n";
            }
        }

        return result;
    }

    private String getFlightsDirect(String departure, String arrival,
                                    LocalDateTime localDepartureDateTime,
                                    LocalDateTime localArrivalDateTime,
                                    Map<String, String> cities) {

        List<Interconnection> directFlights =
                interconnectionsClient.getInterconnections(departure, arrival, localDepartureDateTime, localArrivalDateTime);

        String result = "";
        if (!directFlights.isEmpty()) {
            result = "The flights from " + cities.get(departure) + " to " + cities.get(arrival) + " for this week are :\n\n";
            result = result + "Number      Departure                      Arrival \n";

            for (Interconnection interconnection : directFlights) {
                if (interconnection.getStops() == 0) {
                    for (Leg leg : interconnection.getLegs()) {
                        result = result + leg.getFlightNumber() + "            " +
                                leg.getDepartureDateTime() + "        " +
                                leg.getArrivalDateTime() + "\n";
                    }
                }

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

}
