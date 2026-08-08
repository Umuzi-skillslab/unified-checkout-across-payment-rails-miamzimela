package com.paynestsystem.payment;

public class WalletPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing wallet payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // Simulated payment for the capstone demo.
    }

    @Override
    public String getPaymentType() {
        return "WALLET";
    }
}