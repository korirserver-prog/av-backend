package com.game.aviator.telegram;

import com.game.aviator.telegram.dto.TelegramRequest;
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

//    @PostMapping("/webhook")
//    public ResponseEntity<TelegramResponse> webhook(
////            @RequestBody Map<String, Object> update
//            @RequestBody TelegramResponse telegramResponse
//    ) {
//
//        System.out.println("TELEGRAM UPDATE: " + telegramResponse);
//
//        Object callbackObject = telegramResponse.get("callback_query");
//
//        if (callbackObject instanceof Map<?, ?> callback) {
//
//            Object callbackId = callback.get("id");
//            Object data = callback.get("data");
//            Object from = callback.get("from");
//
//            System.out.println("CALLBACK DATA: " + data);
//            System.out.println("CALLBACK USER: " + from);
//
//            // Tell Telegram that we received the button click
//            if (callbackId != null) {
//                telegramService.answerCallbackQuery(
//                        callbackId.toString(),
//                        "Button pressed! 🎉"
//                );
//            }
//
//            // Handle the button
//            if ("button_1".equals(data)) {
//
//                System.out.println("BUTTON 1 WAS PRESSED");
//
//            } else if ("button_2".equals(data)) {
//
//                System.out.println("BUTTON 2 WAS PRESSED");
//
//            } else if ("button_3".equals(data)) {
//
//                System.out.println("BUTTON 3 WAS PRESSED");
//            }
//        }
//
//        return ResponseEntity.ok().build();
//    }
@PostMapping("/webhook")
public ResponseEntity<Void> webhook(
        @RequestBody Map<String, Object> update
) {
    System.out.println("TELEGRAM UPDATE RECEIVED");

    Object callbackObject = update.get("callback_query");

    if (callbackObject instanceof Map<?, ?> callback) {

        Object callbackId = callback.get("id");
        Object data = callback.get("data");
        Object from = callback.get("from");

        System.out.println("CALLBACK DATA: " + data);
        System.out.println("CALLBACK USER: " + from);

        // Acknowledge the button click so Telegram stops loading.
        if (callbackId != null) {
            try {
                telegramService.answerCallbackQuery(
                        callbackId.toString(),
                        "Button pressed! 🎉"
                );
            } catch (Exception e) {
                System.err.println(
                        "Failed to answer callback: " + e.getMessage()
                );
            }
        }

        // Handle the button action.
        if ("button_1".equals(data)) {
            System.out.println("BUTTON 1 WAS PRESSED");

        } else if ("button_2".equals(data)) {
            System.out.println("BUTTON 2 WAS PRESSED");

        } else if ("button_3".equals(data)) {
            System.out.println("BUTTON 3 WAS PRESSED");

        } else {
            System.out.println("UNKNOWN CALLBACK DATA: " + data);
        }

    } else {
        System.out.println("UPDATE HAS NO CALLBACK QUERY");
    }

    // Return HTTP 200 so Telegram knows the update was received.
    return ResponseEntity.ok().build();
}

    @PostMapping("/checkpayment")
    public ResponseEntity<String> depositRequest(Authentication authentication, @RequestBody TelegramRequest request) {

        String phone = request.phone;
        String name = authentication.getName();
        int amount = request.amount;

        telegramService.sendDepositRequest(name, phone, amount);
//        telegramService.sendTestMessage();

        return ResponseEntity.ok("Deposit request sent");
    }
}