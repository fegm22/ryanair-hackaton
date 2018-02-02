package com.ryanair.hackaton.interconnections.service;

import com.ryanair.hackaton.interconnections.dto.RouteDto;
import com.ryanair.hackaton.interconnections.integration.RoutesApiClient;
import com.ryanair.hackaton.interconnections.model.Airport;
import com.ryanair.hackaton.interconnections.model.Route;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoutesService {

    private RoutesApiClient routesApiClient;

    {
        routesApiClient = new RoutesApiClient(new RestTemplate(), "https://api.ryanair.com/core/3/routes/");
    }

    @Cacheable("CACHE_ROUTES")
    public DirectedGraph<Airport, DefaultEdge> getAvailableRoutes() {
        DirectedGraph<Airport, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        List<RouteDto> routes = routesApiClient.getRoutes();

        routes.stream().forEach(route -> fillGraph(graph, route));

        return graph;
    }

    private void fillGraph(DirectedGraph<Airport, DefaultEdge> graph, RouteDto route) {
        graph.addVertex(route.getAirportFrom());
        graph.addVertex(route.getAirportTo());
        graph.addEdge(route.getAirportFrom(), route.getAirportTo());
    }


    /**
     * This method will return a map of all airports and his connections
     *
     * @return a map of every Airport and his connections in a list.
     */
    public Map<String, Set<String>> getAllAvailableRoutes() {

        List<RouteDto> routes = routesApiClient.getRoutes();

        Map<String, Set<String>> mapRoute = new HashMap<>();

        routes.stream().forEach(route -> addRoute(mapRoute, route.getRoute()));

        return mapRoute;
    }

    private void addRoute(Map<String, Set<String>> mapRoute, Route route) {
        String airportFrom = route.getFrom().getIataCode();
        String airportTo = route.getTo().getIataCode();

        mapRoute.computeIfAbsent(airportFrom, i -> new HashSet<>()).add(airportTo);
    }
}
