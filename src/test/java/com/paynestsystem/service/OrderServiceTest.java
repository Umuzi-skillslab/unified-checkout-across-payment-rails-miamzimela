package com.paynestsystem.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;
import com.paynestsystem.payment.CardPayment;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.WalletPayment;

class OrderServiceTest {

    private final Customer customer = new Customer(1, "Mia Mzimela", "mia.mzimela@umuzi.org");
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void captureConsole() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true));
    }

    @AfterEach
    void restoreConsole() {
        System.setOut(originalOut);
    }

    @Test
    void successfulCheckoutPrintsTheWholeStory() {
        Order order = buildOrder(6004);

        boolean completed = new OrderService().checkout(order, new CardPayment());

        assertTrue(completed);
        assertArrayEquals(new String[] {
                "Processing card payment for R2849.79",
                "Payment successful via CARD",
                "Amount: R2849.79",
                "Order 6004 completed."
        }, consoleLines());
    }

    @Test
    void declinedCheckoutPrintsWhyAndThatTheOrderIsNotCompleted() {
        Order order = buildOrder(6004);

        boolean completed = new OrderService().checkout(order, new WalletPayment("100"));

        assertFalse(completed);
        assertArrayEquals(new String[] {
                "Wallet payment declined: insufficient balance",
                "Payment failed via WALLET",
                "Order 6004 not completed."
        }, consoleLines());
    }

    @Test
    void checkoutRefusesTotalWithAFractionOfACent() {
        Order order = new Order(6001, customer);
        order.addItem(new Product(104, "Test Record", "10.005"), 1);
        RecordingRail rail = new RecordingRail();

        boolean completed = new OrderService().checkout(order, rail);

        assertFalse(completed);
        assertFalse(rail.wasCalled);
    }

    @Test
    void checkoutAcceptsTotalWhoseExtraDecimalsAreZeros() {
        Order order = new Order(6002, customer);
        order.addItem(new Product(105, "Test Record", "849.990"), 1);
        RecordingRail rail = new RecordingRail();

        boolean completed = new OrderService().checkout(order, rail);

        assertTrue(completed);
        assertEquals(849.99, rail.received, 0.001);
    }

    @Test
    void checkoutAcceptsItemsThatAddUpToWholeCents() {
        Order order = new Order(6003, customer);
        order.addItem(new Product(106, "Test Record", "0.005"), 2);
        RecordingRail rail = new RecordingRail();

        boolean completed = new OrderService().checkout(order, rail);

        assertTrue(completed);
        assertEquals(0.01, rail.received, 0.001);
    }

    private Order buildOrder(int id) {
        Order order = new Order(id, customer);
        order.addItem(new Product(101, "The Poet - Bobby Womack", "849.99"), 1);
        order.addItem(new Product(102, "Hot Space - Queen", "999.90"), 2);
        return order;
    }

    // Everything printed so far, one entry per line
    private String[] consoleLines() {
        return captured.toString().trim().split("\\R");
    }

    // Stand-in rail for tests: remembers whether it was called and what amount it was given
    private static class RecordingRail implements PaymentMethod {
        boolean wasCalled;
        double received;

        @Override
        public boolean processPayment(double amount) {
            wasCalled = true;
            received = amount;
            return true;
        }

        @Override
        public String getPaymentType() {
            return "TEST";
        }
    }
}