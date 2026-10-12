package com.example.aviator.telegram;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//private Long adminChatId;
@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {



    private final TelegramService telegramService;

    public TelegramWebhookController(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody Map<String, Object> update
    ) {

        System.out.println("TELEGRAM UPDATE: " + update);

        Object callbackObject = update.get("callback_query");

        if (callbackObject instanceof Map<?, ?> callback) {

            Object callbackId = callback.get("id");
            Object data = callback.get("data");
            Object from = callback.get("from");

            System.out.println("CALLBACK DATA: " + data);
            System.out.println("CALLBACK USER: " + from);

            // Tell Telegram that we received the button click
            if (callbackId != null) {
                telegramService.answerCallbackQuery(
                        callbackId.toString(),
                        "Button pressed! 🎉"
                );
            }

            // Handle the button
            if ("button_1".equals(data)) {

                System.out.println("BUTTON 1 WAS PRESSED");

            } else if ("button_2".equals(data)) {

                System.out.println("BUTTON 2 WAS PRESSED");

            } else if ("button_3".equals(data)) {

                System.out.println("BUTTON 3 WAS PRESSED");
            }
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/depositrequest")
    public ResponseEntity<String> depositRequest(Authentication authentication) {

        String phone = authentication.getName();

        // get user's name from database using phone
        String name = "Ivan Mula";
        telegramService.sendDepositRequest(name, phone);

        return ResponseEntity.ok("Deposit request sent");
    }
}