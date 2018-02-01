package com.ryanair.hackathon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RyanairEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RyanairEurekaServerApplication.class, args);
	}
}
