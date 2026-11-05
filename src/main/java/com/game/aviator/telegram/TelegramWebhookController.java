package com.game.aviator.telegram;

import com.game.aviator.telegram.dto.TelegramRequest;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.game.aviator.deposit.PendingDeposit;
import com.game.aviator.deposit.PendingDepositService;
import java.math.BigDecimal;

import java.util.Map;
//private Long adminChatId;
@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhookController {



    private final TelegramService telegramService;
    private final PendingDepositService pendingDepositService;

    public TelegramWebhookController(TelegramService telegramService,         PendingDepositService pendingDepositService
    ) {
        this.telegramService = telegramService;
        this.pendingDepositService = pendingDepositService;

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
//@PostMapping("/webhook")
//public ResponseEntity<Void> webhook(
//        @RequestBody Map<String, Object> update
//) {
//    System.out.println("TELEGRAM UPDATE RECEIVED");
//
//    Object callbackObject = update.get("callback_query");
//
//    if (callbackObject instanceof Map<?, ?> callback) {
//
//        Object callbackId = callback.get("id");
//        Object data = callback.get("data");
//        Object from = callback.get("from");
//
//        System.out.println("CALLBACK DATA: " + data);
//        System.out.println("CALLBACK USER: " + from);
//
//        // Acknowledge the button click so Telegram stops loading.
//        if (callbackId != null) {
//            try {
//                telegramService.answerCallbackQuery(
//                        callbackId.toString(),
//                        "Button pressed! 🎉"
//                );
//            } catch (Exception e) {
//                System.err.println(
//                        "Failed to answer callback: " + e.getMessage()
//                );
//            }
//        }
//
//        // Handle the button action.
//        if ("button_1".equals(data)) {
//            System.out.println("BUTTON 1 WAS PRESSED");
//
//        } else if ("button_2".equals(data)) {
//            System.out.println("BUTTON 2 WAS PRESSED");
//
//        } else if ("button_3".equals(data)) {
//            System.out.println("BUTTON 3 WAS PRESSED");
//
//        } else {
//            System.out.println("UNKNOWN CALLBACK DATA: " + data);
//        }

//    } else {
//        System.out.println("UPDATE HAS NO CALLBACK QUERY");
//    }
//
//    // Return HTTP 200 so Telegram knows the update was received.
//    return ResponseEntity.ok().build();
//}

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

        String callbackData = data == null ? "" : data.toString();

        System.out.println("CALLBACK DATA: " + callbackData);
        System.out.println("CALLBACK USER: " + from);

        String depositId = null;
        String newStatus = null;

        if (callbackData.startsWith("approve_deposit")) {

            depositId = callbackData
                    .substring("approve_deposit".length())
                    .replaceFirst("^:", "");

            newStatus = "APPROVED";

            System.out.println("APPROVING DEPOSIT: " + depositId);

        } else if (callbackData.startsWith("reject_deposit")) {

            depositId = callbackData
                    .substring("reject_deposit".length())
                    .replaceFirst("^:", "");

            newStatus = "REJECTED";

            System.out.println("REJECTING DEPOSIT: " + depositId);

        }

        if (depositId != null && !depositId.isBlank()) {

            boolean updated = pendingDepositService.updateStatus(
                    depositId,
                    newStatus
            );

            if (updated) {
                System.out.println(
                        "DEPOSIT STATUS UPDATED: "
                                + depositId + " -> " + newStatus
                );
            } else {
                System.out.println(
                        "FAILED TO UPDATE DEPOSIT: " + depositId
                );
            }

        } else {
            System.out.println("UNKNOWN CALLBACK DATA: " + callbackData);
        }

        if (callbackId != null) {
            telegramService.answerCallbackQuery(
                    callbackId.toString(),
                    "APPROVED".equals(newStatus)
                            ? "Deposit approved!"
                            : "REJECTED".equals(newStatus)
                            ? "Deposit rejected!"
                            : "Unknown action"
            );
        }

    } else {
        System.out.println("UPDATE HAS NO CALLBACK QUERY");
    }

    return ResponseEntity.ok().build();
}

//    @PostMapping("/checkpayment")
//    public ResponseEntity<String> depositRequest(Authentication authentication, @RequestBody TelegramRequest request) {
//
//        String phone = request.phone;
//        String name = authentication.getName();
//        int amount = request.amount;
//
//        telegramService.sendDepositRequest(name, phone, amount);
////        telegramService.sendTestMessage();
//
//        return ResponseEntity.ok("Deposit request sent");
//    }
//@PostMapping("/checkpayment")
//public ResponseEntity<String> depositRequest(
//        Authentication authentication,
//        @RequestBody TelegramRequest request
//) {
//
//    String phone = request.phone;
//    String name = authentication.getName();
//    BigDecimal amount = BigDecimal.valueOf(request.amount);
//
//    String depositId = pendingDepositService.generateId();
//
//    PendingDeposit deposit = new PendingDeposit(
//            depositId,
//            name,
//            phone,
//            amount,
//            authentication.getName()
//    );
//
//    pendingDepositService.create(deposit);
//
////    telegramService.sendDepositRequest(name, phone, request.amount);
//    telegramService.sendDepositRequest(
//            depositId,
//            name,
//            phone,
//            request.amount
//    );
//    return ResponseEntity.ok("Deposit request sent");
//}

@PostMapping("/checkpayment")
public ResponseEntity<?> depositRequest(
        Authentication authentication,
        @RequestBody TelegramRequest request
) {
    String phone = request.phone;
    String name = authentication.getName();
    int amount = request.amount;

    // Generate a unique reference
    String depositId = pendingDepositService.generateId();

    // Keep the request in memory
    PendingDeposit deposit = new PendingDeposit(
            depositId,
            name,
            phone,
            BigDecimal.valueOf(amount),
            authentication.getName(),
            "PENDING"
    );

    pendingDepositService.create(deposit);

    // Send Telegram notification with the reference ID
    telegramService.sendDepositRequest(
            depositId,
            name,
            phone,
            amount
    );

    // Return the reference to the frontend
    return ResponseEntity.ok(
            Map.of(
                    "message", "Deposit request sent",
                    "depositId", depositId,
                    "status", "PENDING"
            )
    );
}


    @PostMapping("/payment-status")
    public ResponseEntity<?> paymentStatus(
            @RequestBody Map<String, String> request) {

        String depositId = request.get("depositId");

        System.out.println("PAYMENT STATUS CHECK: " + depositId);

        if (depositId == null || depositId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Deposit ID is required"));
        }

        PendingDeposit deposit = pendingDepositService.get(depositId);

        if (deposit == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Deposit not found"));
        }

        return ResponseEntity.ok(
                Map.of("status", deposit.getStatus())
        );
    }

}