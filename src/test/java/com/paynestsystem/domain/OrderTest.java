package com.paynestsystem.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Order domain class.
 *
 * Think of each test like a tiny experiment:
 *   Arrange  → set up the objects you need
 *   Act      → call the method you want to test
 *   Assert   → check the result is what you expected
 *
 * Run all tests with: mvn test
 */
class OrderTest {

    private Customer customer;
    private Product laptop;
    private Product mouse;

    @BeforeEach
    void setUp() {
        customer = new Customer(1, "Mia Mzimela", "mia@umuzi.org");
        laptop   = new Product(101, "Laptop", "12000.00");
        mouse    = new Product(102, "Mouse",    "350.00");
    }

    @Test
    @DisplayName("An empty order has a grand total of R0.00")
    void emptyOrderTotalIsZero() {
        Order order = new Order(1, customer);
        assertEquals(BigDecimal.ZERO, order.calculateTotal());
    }

    @Test
    @DisplayName("Single item: total equals price x quantity")
    void singleItemTotal() {
        Order order = new Order(1, customer);
        order.addItem(laptop, 1);
        assertEquals(new BigDecimal("12000.00"), order.calculateTotal());
    }

    @Test
    @DisplayName("Single item with quantity > 1: total multiplies correctly")
    void singleItemMultipleQuantity() {
        Order order = new Order(1, customer);
        order.addItem(mouse, 3);
        // 3 x R350.00 = R1050.00
        assertEquals(new BigDecimal("1050.00"), order.calculateTotal());
    }

    @Test
    @DisplayName("Multiple different items: grand total equals sum of line subtotals")
    void multipleItemsGrandTotalEqualsSum() {
        Order order = new Order(1, customer);
        order.addItem(laptop, 1); // R12000.00
        order.addItem(mouse,  2); // R700.00
        assertEquals(new BigDecimal("12700.00"), order.calculateTotal());
    }

    @Test
    @DisplayName("addItem increases the list size correctly")
    void addItemIncreasesListSize() {
        Order order = new Order(1, customer);
        assertEquals(0, order.getItems().size());
        order.addItem(laptop, 1);
        assertEquals(1, order.getItems().size());
        order.addItem(mouse, 2);
        assertEquals(2, order.getItems().size());
    }

    @Test
    @DisplayName("Grand total matches the sum of individual line subtotals")
    void grandTotalMatchesSumOfLineTotals() {
        Order order = new Order(1, customer);
        order.addItem(laptop, 2);
        order.addItem(mouse, 5);

        BigDecimal manualSum = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            manualSum = manualSum.add(item.calculateTotal());
        }
        assertEquals(manualSum, order.calculateTotal());
    }

    @Test
    @DisplayName("Quantity of 0 throws IllegalArgumentException")
    void zeroQuantityThrowsException() {
        Order order = new Order(1, customer);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(laptop, 0));
    }

    @Test
    @DisplayName("Negative quantity throws IllegalArgumentException")
    void negativeQuantityThrowsException() {
        Order order = new Order(1, customer);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(laptop, -5));
    }

    @Test
    @DisplayName("Null product throws IllegalArgumentException")
    void nullProductThrowsException() {
        Order order = new Order(1, customer);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(null, 1));
    }

    @Test
    @DisplayName("getItems() returns an unmodifiable list — callers cannot corrupt the order")
    void getItemsIsUnmodifiable() {
        Order order = new Order(1, customer);
        order.addItem(laptop, 1);

        List<OrderItem> items = order.getItems();
        assertThrows(UnsupportedOperationException.class,
                () -> items.add(new OrderItem(mouse, 1)));
    }

    @Test
    @DisplayName("Creating an Order with a null customer throws IllegalArgumentException")
    void nullCustomerThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1, null));
    }
}