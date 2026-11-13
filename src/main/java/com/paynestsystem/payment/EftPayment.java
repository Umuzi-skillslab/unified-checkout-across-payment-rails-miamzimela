package com.paynestsystem.payment;

public class EftPayment implements PaymentMethod {

    private static final double EFT_MINIMUM = 10;

    @Override
    public boolean processPayment(double amount) {
        if (!isValidAmount(amount)) {
            System.out.println("EFT payment rejected: amount must be greater than R0");
            return false;
        }

        if (amount < EFT_MINIMUM) {
            System.out.println("EFT payment declined: amount is below the R10 minimum");
            return false;
        }

        System.out.println("Processing EFT payment for R" + String.format(java.util.Locale.US, "%.2f", amount));
        return true; // simulated payment for the demo
    }

    @Override
    public String getPaymentType() {
        return "EFT";
    }
}