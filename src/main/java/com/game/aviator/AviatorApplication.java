package com.game.aviator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AviatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                AviatorApplication.class,
                args
        );
    }
}