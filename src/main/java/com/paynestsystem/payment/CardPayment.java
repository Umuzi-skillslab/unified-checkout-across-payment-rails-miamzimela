package com.paynestsystem.payment;

public class CardPayment implements PaymentMethod {

    private static final double CARD_LIMIT = 50000;

    @Override
    public boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("Card payment rejected: amount must be greater than R0");
            return false;
        }

        if (amount > CARD_LIMIT) {
            System.out.println("Card payment declined: amount is above the R50000 card limit");
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