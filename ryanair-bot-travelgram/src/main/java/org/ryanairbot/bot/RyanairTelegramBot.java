package org.ryanairbot.bot;

import org.ryanairbot.service.RyanairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

/**
 * francisco . 2018
 **/
@Service
public class RyanairTelegramBot {

    @Autowired
    public RyanairTelegramBot(final RyanairService ryanairService,
                              @Value("${ryanair.token}") final String token,
                              @Value("${ryanair.user}") final String botUsername) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new TelegramHandlers(ryanairService, token, botUsername));
    }
}
