package com.ryanair.hackaton.interconnections.integration;

import org.springframework.web.client.RestTemplate;

public abstract class BaseClient {

    protected RestTemplate template;
    protected String baseUrl;

    public BaseClient(RestTemplate template, String baseUrl) {
        this.template = template;
        this.baseUrl = baseUrl;
    }
}
