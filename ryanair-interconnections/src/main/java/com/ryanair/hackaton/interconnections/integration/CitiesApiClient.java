package com.ryanair.hackaton.interconnections.integration;

import com.ryanair.hackaton.interconnections.dto.CitiesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

public class CitiesApiClient extends BaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CitiesApiClient.class);

    public CitiesApiClient(RestTemplate template, String baseUrl) {
        super(template, baseUrl);
    }

    public List<CitiesDto> getAirports() {
        try {
            ParameterizedTypeReference<List<CitiesDto>> type = new ParameterizedTypeReference<List<CitiesDto>>() {
            };
            ResponseEntity<List<CitiesDto>> instance = template.exchange(baseUrl, HttpMethod.GET, null, type);

            LOGGER.debug("Got response from Routes API " + instance.getStatusCode());

            if (instance.getStatusCode() == HttpStatus.OK) {
                return instance.getBody();
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            LOGGER.debug("Unable to get routes from Routes API", e);
            return Collections.emptyList();
        }
    }

}
