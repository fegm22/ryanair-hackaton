package com.ryanair.hackaton.parser;

import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * francisco . 2018
 **/
public class MessageParserTest {

    @Test
    public void shouldFindFareExternalService() {
        final String fareMessage = "fare MAD DUB 2018-01-20";
        MessageParser fareMessageParser = new MessageParser(fareMessage);
        String keyword = fareMessageParser.getKeyword();
        assertThat(keyword).isEqualToIgnoringCase("FARE");
        Map<String, Object> fareMapParameters = fareMessageParser.getParameters();
        assertThat(fareMapParameters.keySet())
                .contains("origin", "destination", "date")
                .doesNotContain("name");
    }


    @Test
    public void shouldFindFlightStatExternalService() {
        final String flightStatusMessage = "flight FR7373";
        MessageParser flightStatusMessageParser = new MessageParser(flightStatusMessage);
        String keyword = flightStatusMessageParser.getKeyword();
        assertThat(keyword).isEqualToIgnoringCase("FLIGHT");
        Map<String, Object> fareMapParameters = flightStatusMessageParser.getParameters();
        assertThat(fareMapParameters.keySet())
                .doesNotContain("origin", "destination", "date")
                .doesNotContain("name")
                .contains("flightNumber");
    }

}