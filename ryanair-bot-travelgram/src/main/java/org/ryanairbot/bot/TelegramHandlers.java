package org.ryanairbot.bot;

import org.ryanairbot.service.RyanairService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Optional;

@Service
public class TelegramHandlers extends TelegramLongPollingBot {

    private final RyanairService ryanairService;

    private final String token;

    private final String botUsername;

    public TelegramHandlers(RyanairService ryanairService/*, String token, String botUsername*/) {
        this.ryanairService = ryanairService;
        this.token = "506346062:AAHPvBprq65xTow0cBsls5VS88FZyCeLO20";
        this.botUsername = "ryanair";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Optional.ofNullable(update)
                .map(Update::getMessage)
                .ifPresent(message -> {
                    this.sendMessage(message.getChatId().toString(), ryanairService.processMessage(message.getText()));
                });
    }

    private void sendMessage(String chatId, String text) {
        if (chatId == null || text == null) {
            return;
        }

        SendMessage sendMessageRequest = new SendMessage()
                .setChatId(chatId)
                .enableMarkdown(true)
                .setText(text);

        try {
            super.sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

}
