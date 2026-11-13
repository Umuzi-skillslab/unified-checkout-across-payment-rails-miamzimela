package com.paynestsystem.payment;

public interface PaymentMethod {
    boolean processPayment(double amount);

    String getPaymentType();

    // Same rule for every rail, so it lives here once
    default boolean isValidAmount(double amount) {
        return amount > 0;
    }
}