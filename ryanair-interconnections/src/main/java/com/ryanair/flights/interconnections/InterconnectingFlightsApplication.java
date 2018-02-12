package com.ryanair.flights.interconnections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan (basePackages = "com.ryanair.flights.interconnections" )
public class InterconnectingFlightsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterconnectingFlightsApplication.class, args);
    }
}
