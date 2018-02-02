package com.ryanair.hackaton.interconnections.service;

import com.ryanair.hackaton.interconnections.dto.CitiesDto;
import com.ryanair.hackaton.interconnections.integration.CitiesApiClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CitiesServices {

    private CitiesApiClient citiesApiClient = new CitiesApiClient(new RestTemplate(), "https://api.ryanair.com/core/3/airports/");

    @Cacheable("CACHE_AIRPORTS")
    public Map<String, String> getAllAvailableAirports() {
        Map<String, String> map = new HashMap<>();
        List<CitiesDto> airports = citiesApiClient.getAirports();

        airports.stream().forEach(CitiesDto -> addCity(map, CitiesDto));

        return map;
    }

    private void addCity(Map<String, String> map, CitiesDto city) {
        String citiCode = city.getCity().getCitiCodeCode().replace("_", " ");
        String iataCode = city.getCity().getIataCode();

        map.putIfAbsent(iataCode, citiCode);
        map.putIfAbsent(citiCode, iataCode);
    }
}
