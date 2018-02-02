package com.ryanair.hackathon.service.impl;

import com.ryanair.hackathon.client.FlightStatsClient;
import com.ryanair.hackathon.service.FlightStatusService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.cloud.stream.messaging.Source;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Source;

/**
 * francisco . 2018
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class FlightInfoServiceImplTest {

    @MockBean
    private FlightStatsClient flightStatsClient;

    @MockBean
    private Source outputSource;

    @Autowired
    private FlightStatusService flightStatusService;

    @Test
    public void shouldFindFlightStats() {


    }
}