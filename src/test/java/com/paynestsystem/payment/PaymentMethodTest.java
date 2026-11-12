package com.paynestsystem.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PaymentMethodTest {

    @Test
    void cardPaymentProcessesSuccessfullyAndReportsCorrectType() {
        PaymentMethod card = new CardPayment();

        assertTrue(card.processPayment(12400));
        assertEquals("CARD", card.getPaymentType());
    }

    @Test
    void eftPaymentProcessesSuccessfullyAndReportsCorrectType() {
        PaymentMethod eft = new EftPayment();

        assertTrue(eft.processPayment(12400));
        assertEquals("EFT", eft.getPaymentType());
    }

    @Test
    void walletPaymentProcessesSuccessfullyAndReportsCorrectType() {
        PaymentMethod wallet = new WalletPayment(20000);

        assertTrue(wallet.processPayment(12400));
        assertEquals("WALLET", wallet.getPaymentType());
    }

    @Test
    void checkoutCanUseDifferentPaymentMethodsThroughSameProcessor() {
        PaymentProcessor processor = new PaymentProcessor();

        PaymentMethod card = new CardPayment();
        PaymentMethod wallet = new WalletPayment(20000);

        assertTrue(processor.processPayment(card, 12400));
        assertTrue(processor.processPayment(wallet, 12400));
    }
}