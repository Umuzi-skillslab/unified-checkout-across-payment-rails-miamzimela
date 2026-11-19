package com.paynestsystem.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class WalletPaymentTest {

    @Test
    void smallPaymentsUseUpTheBalanceExactly() {
        WalletPayment wallet = new WalletPayment("0.30");

        assertTrue(wallet.processPayment(0.10));
        assertTrue(wallet.processPayment(0.10));
        assertTrue(wallet.processPayment(0.10));

        assertEquals(0, BigDecimal.ZERO.compareTo(wallet.getBalance()));
        assertFalse(wallet.processPayment(0.10));
    }

    @Test
    void balanceDropsByTheAmountPaid() {
        WalletPayment wallet = new WalletPayment("100.00");

        wallet.processPayment(25.50);

        assertEquals(0, new BigDecimal("74.50").compareTo(wallet.getBalance()));
    }

    @Test
    void rejectsNegativeOpeningBalance() {
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment("-1"));
    }
}