package com.paynestsystem.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderItem> items;

    public Order(int id, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("An order must belong to a customer.");
        }
        this.id = id;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    // Only way to add items — validates product and quantity before adding
    public void addItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        items.add(new OrderItem(product, quantity));
    }

    // Sums every line subtotal — empty order returns R0.00
    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.calculateTotal());
        }
        return total;
    }

    // Read-only view — callers can look but cannot modify the list
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getId()            { return id; }
    public Customer getCustomer() { return customer; }
}