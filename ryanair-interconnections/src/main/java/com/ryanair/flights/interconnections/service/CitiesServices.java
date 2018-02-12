package com.ryanair.flights.interconnections.service;

import com.ryanair.flights.interconnections.domain.CitiesDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CitiesServices {

    public Map<String, String> getAllAvailableAirports() {
        Map<String, String> map = new HashMap<>();
        List<CitiesDto> airports = getAirports();

        airports.stream().forEach(CitiesDto -> addCity(map, CitiesDto));

        return map;
    }

    private void addCity(Map<String, String> map, CitiesDto city) {
        String citiCode = city.getCity().getCitiCodeCode().replace("_", " ");
        String iataCode = city.getCity().getIataCode();

        map.putIfAbsent(iataCode, citiCode);
        map.putIfAbsent(citiCode, iataCode);
    }

    private List<CitiesDto> getAirports() {
        try {
            final String baseUrl = "https://api.ryanair.com/core/3/airports/";
            ParameterizedTypeReference<List<CitiesDto>> type = new ParameterizedTypeReference<List<CitiesDto>>() {
            };

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<CitiesDto>> instance = restTemplate.exchange(baseUrl, HttpMethod.GET, null, type);


            if (instance.getStatusCode() == HttpStatus.OK) {
                return instance.getBody();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
