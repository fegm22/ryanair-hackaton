package com.ryanair.hackaton.controller;


import com.ryanair.hackaton.factory.CommandServiceFactory;
import com.ryanair.hackaton.model.flightstatus.CommandResponse;
import com.ryanair.hackaton.parser.MessageParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/commands")
public class CommandController {

    private final CommandServiceFactory commandServiceFactory;

    public CommandController(CommandServiceFactory commandServiceFactory) {
        this.commandServiceFactory = commandServiceFactory;
    }

    @PostMapping
    public ResponseEntity processMessage(@RequestBody String message) {

        MessageParser messageParser = new MessageParser(message);
        CommandResponse result = commandServiceFactory
                .getClient(messageParser.getKeyword())
                .process(messageParser.getParameters());

        return ResponseEntity.ok(result);
    }

}
