package com.example.aviator.payment;

import com.example.aviator.payment.dto.AirtelPaymentRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airtel")
public class AirtelPaymentController {

    private final AirtelPaymentService paymentService;

    public AirtelPaymentController(
            AirtelPaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public String initiatePayment(
            @Valid @RequestBody AirtelPaymentRequest request
    ) {

        return paymentService.initiatePayment(request);
    }

    @GetMapping("/status/{transactionId}")
    public String paymentStatus(
            @PathVariable String transactionId
    ) {

        return paymentService.checkPaymentStatus(
                transactionId
        );
    }
}