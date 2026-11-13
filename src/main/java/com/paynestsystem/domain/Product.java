package com.paynestsystem.domain;

import java.math.BigDecimal;

public class Product {
    private final int id;
    private final String name;
    private final BigDecimal price;

    public Product(int id, String name, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be zero or a positive value.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Convenience constructor — accepts a plain string e.g. "849.99"
    public Product(int id, String name, String price) {
        this(id, name, new BigDecimal(price));
    }

    public int getId()           { return id; }
    public String getName()      { return name; }
    public BigDecimal getPrice() { return price; }
}