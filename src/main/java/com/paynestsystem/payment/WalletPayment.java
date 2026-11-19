package com.paynestsystem.payment;

import java.math.BigDecimal;

public class WalletPayment implements PaymentMethod {

    private BigDecimal balance;

    public WalletPayment(BigDecimal openingBalance) {
        if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Opening balance must be zero or a positive value.");
        }
        this.balance = openingBalance;
    }

    // Convenience constructor, accepts a plain string e.g. "500.00"
    public WalletPayment(String openingBalance) {
        this(new BigDecimal(openingBalance));
    }

    @Override
    public boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("Wallet payment rejected: amount must be greater than R0");
            return false;
        }

        // The contract hands us a double, so this is the one place it becomes an exact value
        BigDecimal charge = BigDecimal.valueOf(amount);

        if (charge.compareTo(balance) > 0) {
            System.out.println("Wallet payment declined: insufficient balance");
            return false;
        }

        balance = balance.subtract(charge);
        System.out.println("Processing wallet payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // simulated payment for the demo
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    public BigDecimal getBalance() {
        return balance;
    }
}