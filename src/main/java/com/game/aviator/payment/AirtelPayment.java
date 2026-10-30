package com.game.aviator.payment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AirtelPayment {

    public static void main(String[] args) throws Exception {

        URL obj = new URL(
                "https://openapiuat.airtel.co.zm/standard/v1/payments/{id}"
        );

        HttpURLConnection con =
                (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        con.getInputStream()
                )
        );

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        System.out.println(response);
    }
}