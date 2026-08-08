package com.paynestsystem.payment;

public class EftPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing EFT payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // Simulated payment for the capstone demo.
    }

    @Override
    public String getPaymentType() {
        return "EFT";
    }
}