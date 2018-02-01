package org.ryanairbot.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("COMMAND-SERVICE")
public interface CommandService {

    @PostMapping("/commands")
    String processMessage(@RequestBody String message);

}
