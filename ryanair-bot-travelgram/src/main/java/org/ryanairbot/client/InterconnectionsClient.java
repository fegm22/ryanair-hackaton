package org.ryanairbot.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("RYANAIR-INTERCONNECTIONS")
public interface InterconnectionsClient {

    @GetMapping("/commands")
    String processMessage(@RequestBody String message);




}
