package com.ryanair.hackaton.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 * francisco . 2018
 **/
public class MessageParser {

    private final String message;

    public MessageParser(String message) {
        this.message = message;
    }

    public Map<String, Object> getParameters() {
        ExternalService externalService = getExternalService();
        List<String> values = getServiceParameters();

        if (externalService.getParameters().size() != values.size()) {
            throw new RuntimeException("The number of parameters does not match");
        }

        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < values.size(); i++) {
            parameters.put(externalService.getParameters().get(i), values.get(i));
        }

        return parameters;
    }

    private List<String> getServiceParameters() {
        List<String> values = asList(message.split(" "));
        return values.subList(1, values.size());
    }

    private ExternalService getExternalService() {
        return ExternalService.findByName(getKeyword());
    }

    public String getKeyword() {
        return Optional.ofNullable(this.message).map(s -> s.split(" ")[0]).orElse(null);
    }
}

