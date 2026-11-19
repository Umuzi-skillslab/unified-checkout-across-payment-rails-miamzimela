package com.paynestsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;
import com.paynestsystem.payment.PaymentMethod;

class OrderServiceTest {

    private final Customer customer = new Customer(1, "Mia Mzimela", "mia.mzimela@umuzi.org");

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