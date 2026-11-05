
package com.game.aviator.deposit;

import java.math.BigDecimal;

public class PendingDeposit {

    private final String id;
    private final String name;
    private final String phone;
    private final BigDecimal amount;
    private final String username;
    private  String status;

    public PendingDeposit(
            String id,
            String name,
            String phone,
            BigDecimal amount,
            String username,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.username = username;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUsername() {
        return username;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
