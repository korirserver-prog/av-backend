//package com.game.aviator.game;
//
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Component
//public class CrashPointGenerator {
//
//    private final BigDecimal[] crashPoints = {
//            new BigDecimal("1.50"),
//            new BigDecimal("2.00"),
//            new BigDecimal("3.25"),
//            new BigDecimal("5.00"),
//            new BigDecimal("2.50"),
//            new BigDecimal("4.00")
//    };
//
//    private final AtomicInteger index = new AtomicInteger(0);
//
//    public BigDecimal generate() {
//
//        int currentIndex =
//                index.getAndUpdate(i ->
//                        (i + 1) % crashPoints.length
//                );
//
//        return crashPoints[currentIndex];
//    }
//}


package com.game.aviator.game;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@Component
public class CrashPointGenerator {

    private final SecureRandom random = new SecureRandom();

    // 3% house edge
    private static final double HOUSE_EDGE = 0.03;

    // Maximum crash multiplier
    private static final BigDecimal MAX_CRASH_POINT =
            new BigDecimal("1000.00");

    /**
     * Generates a completely new crash point.
     */
    private BigDecimal generateCrashPoint() {

        double randomValue = random.nextDouble();

        double multiplier =
                (1.0 - HOUSE_EDGE) / (1.0 - randomValue);

        BigDecimal crashPoint = BigDecimal.valueOf(multiplier)
                .setScale(2, RoundingMode.DOWN);

        // Minimum crash point is 1.00
        if (crashPoint.compareTo(BigDecimal.ONE) < 0) {
            crashPoint = BigDecimal.ONE;
        }

        // Prevent excessively high multipliers
        if (crashPoint.compareTo(MAX_CRASH_POINT) > 0) {
            crashPoint = MAX_CRASH_POINT;
        }

        return crashPoint;
    }

    /**
     * Called whenever a new game round needs a crash point.
     */
    public BigDecimal generate() {
        return generateCrashPoint();
    }
}
