package com.game.aviator.wallet;

import com.game.aviator.wallet.dto.WalletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.game.aviator.wallet.dto.DepositRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.game.aviator.wallet.dto.TransactionResponse;
import com.game.aviator.wallet.dto.WithdrawalRequest;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<WalletResponse> getWallet(
            Authentication authentication
    ) {

        String username = authentication.getName();

        WalletResponse response =
                walletService.getWallet(username);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<WalletResponse> deposit(
            Authentication authentication,
            @Valid @RequestBody DepositRequest request
    ) {

        String username = authentication.getName();

        WalletResponse response =
                walletService.deposit(
                        username,
                        request.amount()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            Authentication authentication
    ) {

        String username = authentication.getName();

        List<TransactionResponse> transactions =
                walletService.getTransactions(username);

        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WalletResponse> withdraw(
            Authentication authentication,
            @Valid @RequestBody WithdrawalRequest request
    ) {

        String username = authentication.getName();

        WalletResponse response =
                walletService.withdraw(
                        username,
                        request.amount()
                );

        return ResponseEntity.ok(response);
    }
}