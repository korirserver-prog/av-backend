package com.game.aviator.game;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CrashPointGenerator {

    private final BigDecimal[] crashPoints = {
            new BigDecimal("1.50"),
            new BigDecimal("2.00"),
            new BigDecimal("3.25"),
            new BigDecimal("5.00"),
            new BigDecimal("2.50"),
            new BigDecimal("4.00")
    };

    private final AtomicInteger index = new AtomicInteger(0);

    public BigDecimal generate() {

        int currentIndex =
                index.getAndUpdate(i ->
                        (i + 1) % crashPoints.length
                );

        return crashPoints[currentIndex];
    }
}