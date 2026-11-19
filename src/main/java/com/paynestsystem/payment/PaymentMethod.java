package com.paynestsystem.payment;

public interface PaymentMethod {
    boolean processPayment(double amount);

    String getPaymentType();

    // Same rule for every rail, so it lives here once
    default boolean isValidAmount(double amount) {
        return amount > 0;
    }

    // Runs the shared rule and prints the rejection, so rails don't repeat it
    default boolean checkAmount(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println(getPaymentType() + " payment rejected: amount must be greater than R0");
            return false;
        }
        return true;
    }
}