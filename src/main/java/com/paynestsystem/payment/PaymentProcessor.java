package com.paynestsystem.payment;

public class PaymentProcessor {

    // Works with any PaymentMethod, so a new rail needs no changes here
    public boolean processPayment(PaymentMethod method, double amount) {
        boolean success = method.processPayment(amount);

        if (success) {
            System.out.println("Payment successful via " + method.getPaymentType());
            System.out.println("Amount: R" + String.format(java.util.Locale.US, "%.2f", amount));
        } else {
            System.out.println("Payment failed via " + method.getPaymentType());
        }

        return success;
    }
}