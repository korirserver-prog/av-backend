package com.example.aviator.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AirtelAuthService {

    @Value("${airtel.base-url}")
    private String baseUrl;

    @Value("${airtel.client-id}")
    private String clientId;

    @Value("${airtel.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AirtelAuthService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getAccessToken() {

        String url =
                baseUrl + "/auth/oauth2/token";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_FORM_URLENCODED
        );

        MultiValueMap<String, String> body =
                new LinkedMultiValueMap<>();

        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        String.class
                );

        try {

            JsonNode json =
                    objectMapper.readTree(
                            response.getBody()
                    );

            return json
                    .get("access_token")
                    .asText();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to obtain Airtel access token",
                    e
            );
        }
    }
}