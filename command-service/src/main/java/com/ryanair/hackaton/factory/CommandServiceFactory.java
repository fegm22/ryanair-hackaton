package com.ryanair.hackaton.factory;

import com.ryanair.hackaton.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandServiceFactory {

    @Autowired
    private List<CommandService> commandServiceList;

    public <T> CommandService<T> getClient(String keyword) {
        return commandServiceList.stream()
                .filter(c -> c.match(keyword).isPresent())
                .findAny()
                .orElse(null);
    }
}
