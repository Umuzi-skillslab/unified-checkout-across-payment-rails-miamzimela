package com.paynestsystem.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    // Using a real record from the catalogue as test data
    private final Product miles = new Product(103, "Kind of Blue - Miles Davis", "649.50");

    @Test
    @DisplayName("calculateTotal() returns price x quantity exactly")
    void calculateTotalIsExact() {
        OrderItem item = new OrderItem(miles, 2);
        // 2 x R649.50 = R1299.00
        assertEquals(new BigDecimal("1299.00"), item.calculateTotal());
    }

    @Test
    @DisplayName("Quantity of 1 gives exactly the unit price")
    void quantityOneEqualsUnitPrice() {
        OrderItem item = new OrderItem(miles, 1);
        assertEquals(miles.getPrice(), item.calculateTotal());
    }

    @Test
    @DisplayName("Null product throws IllegalArgumentException")
    void nullProductThrows() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(null, 1));
    }

    @Test
    @DisplayName("Quantity of 0 throws IllegalArgumentException")
    void zeroQuantityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(miles, 0));
    }

    @Test
    @DisplayName("Negative quantity throws IllegalArgumentException")
    void negativeQuantityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(miles, -1));
    }
}