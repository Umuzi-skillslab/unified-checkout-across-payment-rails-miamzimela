package com.paynestsystem.payment;

/**
 * Accepts any PaymentMethod implementation and processes a payment against it.
 * This class never checks which concrete rail it's talking to — it only relies
 * on the PaymentMethod contract, so new rails (e.g. BuyNowPayLaterPayment) can
 * be added later without any changes here.
 */
public class PaymentProcessor {

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