package com.game.aviator.wallet;

import com.game.aviator.user.User;
import com.game.aviator.user.UserRepository;
import com.game.aviator.wallet.dto.WalletResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import com.game.aviator.wallet.dto.TransactionResponse;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public WalletService(
            WalletRepository walletRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public WalletResponse getWallet(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() ->
                        walletRepository.save(new Wallet(user)));

        return new WalletResponse(
                user.getId(),
                wallet.getBalance(),
                "KES"
        );
    }

    @Transactional
    public WalletResponse deposit(
            String username,
            BigDecimal amount
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() ->
                        walletRepository.save(new Wallet(user)));

        BigDecimal newBalance =
                wallet.getBalance().add(amount);

        wallet.setBalance(newBalance);

        walletRepository.save(wallet);

        Transaction transaction = new Transaction(
                wallet,
                TransactionType.DEPOSIT,
                amount,
                newBalance
        );

        transactionRepository.save(transaction);

        return new WalletResponse(
                user.getId(),
                newBalance,
                "KES"
        );
    }

    public List<TransactionResponse> getTransactions(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Wallet not found"));

        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public WalletResponse withdraw(
            String username,
            BigDecimal amount
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Wallet not found"));

        BigDecimal currentBalance = wallet.getBalance();

        if (amount.compareTo(currentBalance) > 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        BigDecimal newBalance =
                currentBalance.subtract(amount);

        wallet.setBalance(newBalance);

        walletRepository.save(wallet);

        Transaction transaction = new Transaction(
                wallet,
                TransactionType.WITHDRAWAL,
                amount.negate(),
                newBalance
        );

        transactionRepository.save(transaction);

        return new WalletResponse(
                user.getId(),
                newBalance,
                "KES"
        );
    }
}