package com.example.aviator.bet;

import com.example.aviator.bet.dto.BetResponse;
import com.example.aviator.bet.dto.PlaceBetRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping
    public ResponseEntity<BetResponse> placeBet(
            Authentication authentication,
            @Valid @RequestBody PlaceBetRequest request
    ) {

        String username = authentication.getName();

        BetResponse response =
                betService.placeBet(
                        username,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{betId}/cashout")
    public ResponseEntity<BetResponse> cashOut(
            Authentication authentication,
            @PathVariable Long betId
    ) {

        String username = authentication.getName();

        BetResponse response =
                betService.cashOut(
                        username,
                        betId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BetResponse>> getMyBets(
            Authentication authentication
    ) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                betService.getMyBets(username)
        );
    }
}