package com.paynestsystem.payment;

public class CardPayment implements PaymentMethod {

    @Override
    public boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("Card payment rejected: amount must be greater than R0");
            return false;
        }

        System.out.println("Processing card payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // simulated payment for the demo
    }

    @Override
    public String getPaymentType() {
        return "CARD";
    }
}