package com.paynestsystem.payment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RailRulesTest {

    @Test
    void cardAcceptsAmountAtTheLimit() {
        assertTrue(new CardPayment().processPayment(50000));
    }

    @Test
    void cardDeclinesAmountAboveTheLimit() {
        assertFalse(new CardPayment().processPayment(50000.01));
    }

    @Test
    void eftAcceptsAmountAtTheMinimum() {
        assertTrue(new EftPayment().processPayment(10));
    }

    @Test
    void eftDeclinesAmountBelowTheMinimum() {
        assertFalse(new EftPayment().processPayment(9.99));
    }
}