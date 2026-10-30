package com.game.aviator.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class TelegramService {

    private final RestClient restClient;

    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.admin.chat-id}")
    private String chatId;


//    public TelegramService(RestClient.Builder restClientBuilder) {
//        this.restClient = restClientBuilder.build();
//    }
public TelegramService() {
    this.restClient = RestClient.create();
}

    public void sendDepositRequest(
            String name,
            String phone,
            int amount
    ) {

        String url =
                "https://api.telegram.org/bot"
                        + botToken
                        + "/sendMessage";

        Map<String, Object> approveButton = Map.of(
                "text", "✅ APPROVE",
                "callback_data", "approve_deposit"
        );

        Map<String, Object> rejectButton = Map.of(
                "text", "❌ REJECT",
                "callback_data", "reject_deposit"
        );

        Map<String, Object> keyboard = Map.of(
                "inline_keyboard",
                List.of(
                        List.of(approveButton, rejectButton)
                )
        );

        String message =
                "💰 NEW SUBSCRIPTION REQUEST\n\n" +
                        "👤 Name: " + name + "\n" +
                        "📱 Phone: " + phone + "\n\n" +
                        "📱 Amount: " + amount + "\n\n" +
                        "Please review this subscription.";

        Map<String, Object> body = Map.of(
                "chat_id", chatId,
                "text", message,
                "reply_markup", keyboard
        );

        String response = restClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        System.out.println("DEPOSIT TELEGRAM RESPONSE: " + response);
    }


    public void sendTestMessage() {

        String url =
                "https://api.telegram.org/bot"
                        + botToken
                        + "/sendMessage";

        Map<String, Object> button1 = Map.of(
                "text", "🟢 BUTTON 1",
                "callback_data", "button_1"
        );

        Map<String, Object> button2 = Map.of(
                "text", "🔵 BUTTON 2",
                "callback_data", "button_2"
        );

        Map<String, Object> button3 = Map.of(
                "text", "🔴 BUTTON 3",
                "callback_data", "button_3"
        );

        Map<String, Object> keyboard = Map.of(
                "inline_keyboard",
                List.of(
                        List.of(button1, button2),
                        List.of(button3)
                )
        );

        Map<String, Object> body = Map.of(
                "chat_id", chatId,
                "text", " Deposit Request\n\nfrom user name: \n\n Phone Number: ",
                "reply_markup", keyboard
        );

        String response = restClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        System.out.println("TELEGRAM RESPONSE: " + response);
    }

    public void answerCallbackQuery(String callbackQueryId, String text) {

        String url =
                "https://api.telegram.org/bot" + botToken + "/answerCallbackQuery";

        Map<String, Object> body = Map.of(
                "callback_query_id", callbackQueryId,
                "text", text,
                "show_alert", false
        );

        String response = restClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        System.out.println("CALLBACK RESPONSE: " + response);
    }
}