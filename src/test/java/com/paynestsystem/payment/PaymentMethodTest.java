package com.paynestsystem.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;
import com.paynestsystem.service.OrderService;

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

    @Test
    void railsRejectZeroAndNegativeAmounts() {
        PaymentMethod card = new CardPayment();
        PaymentMethod eft = new EftPayment();
        PaymentMethod wallet = new WalletPayment(1000);

        assertFalse(card.processPayment(0));
        assertFalse(card.processPayment(-50));
        assertFalse(eft.processPayment(0));
        assertFalse(eft.processPayment(-50));
        assertFalse(wallet.processPayment(0));
        assertFalse(wallet.processPayment(-50));
    }

    @Test
    void walletDeclinesWhenAmountExceedsBalance() {
        WalletPayment wallet = new WalletPayment(500);

        boolean paid = wallet.processPayment(600);

        assertFalse(paid);
        assertEquals(500, wallet.getBalance(), 0.001);
    }

    @Test
    void checkoutReturnsFalseWhenWalletCannotCoverTheOrder() {
        Order order = buildOrder();
        WalletPayment wallet = new WalletPayment(100);

        boolean completed = new OrderService().checkout(order, wallet);

        assertFalse(completed);
        assertEquals(100, wallet.getBalance(), 0.001);
    }

    @Test
    void processorPassesExactAmountToTheRail() {
        RecordingRail rail = new RecordingRail();

        new PaymentProcessor().processPayment(rail, 12400);

        assertEquals(12400, rail.received, 0.001);
    }

    @Test
    void checkoutChargesTheOrderTotal() {
        Order order = buildOrder();
        RecordingRail rail = new RecordingRail();

        new OrderService().checkout(order, rail);

        assertEquals(order.calculateTotal().doubleValue(), rail.received, 0.001);
    }

    private Order buildOrder() {
        Customer customer = new Customer(1, "Mia Mzimela", "mia.mzimela@umuzi.org");
        Order order = new Order(5001, customer);
        order.addItem(new Product(101, "The Poet - Bobby Womack", "849.99"), 1);
        order.addItem(new Product(102, "Hot Space - Queen", "999.90"), 2);
        return order;
    }

    // Stand-in rail for tests: it just remembers the amount it was asked to charge
    private static class RecordingRail implements PaymentMethod {
        double received;

        @Override
        public boolean processPayment(double amount) {
            received = amount;
            return true;
        }

        @Override
        public String getPaymentType() {
            return "TEST";
        }
    }
}