package com.paynestsystem.payment;

public class WalletPayment implements PaymentMethod {

    private double balance;

    public WalletPayment(double openingBalance) {
        this.balance = openingBalance;
    }

    @Override
    public boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("Wallet payment rejected: amount must be greater than R0");
            return false;
        }

        if (amount > balance) {
            System.out.println("Wallet payment declined: insufficient balance");
            return false;
        }

        balance -= amount;
        System.out.println("Processing wallet payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // simulated payment for the demo
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }

    public double getBalance() {
        return balance;
    }
}