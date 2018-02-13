package org.ryanairbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class TravelgramBotApplication {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(TravelgramBotApplication.class, args);
    }
}


