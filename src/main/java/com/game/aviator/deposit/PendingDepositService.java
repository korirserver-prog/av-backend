
package com.game.aviator.deposit;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.game.aviator.deposit.PendingDeposit;

@Service
public class PendingDepositService {

    private final ConcurrentHashMap<String, PendingDeposit> deposits =
            new ConcurrentHashMap<>();

    public PendingDeposit create(PendingDeposit deposit) {
        deposits.put(deposit.getId(), deposit);
        return deposit;
    }

    public PendingDeposit get(String id) {
        return deposits.get(id);
    }

    public PendingDeposit remove(String id) {
        return deposits.remove(id);
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public boolean updateStatus(String depositId, String status) {

        PendingDeposit deposit = deposits.get(depositId);

        if (deposit == null) {
            System.out.println("DEPOSIT NOT FOUND: " + depositId);
            return false;
        }

        deposit.setStatus(status);

        System.out.println("DEPOSIT STATUS UPDATED: "
                + depositId + " -> " + status);

        return true;
    }
}
