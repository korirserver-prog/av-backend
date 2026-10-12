package com.example.aviator.telegram;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram/test")
public class TelegramTestController {

    private final TelegramService telegramService;

    public TelegramTestController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping
    public String sendTest() {

        Long chatId = 8573548784L;

        telegramService.sendTestMessage(chatId);

        return "Telegram message sent";
    }
}