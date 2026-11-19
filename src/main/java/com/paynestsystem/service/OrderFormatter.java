package com.paynestsystem.service;

import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Builds a human-readable receipt string for an Order.
 *
 * Why a separate class?
 * 1. You can TEST the output without capturing System.out.
 * 2. Later you can add an HTML or JSON formatter alongside this one —
 *    the Order class never needs to change.
 * 3. OrderService stays clean: it asks the formatter for text,
 *    then decides where to send it.
 *
 * Rounding policy:
 * All amounts are rounded to 2 decimal places using HALF_UP
 * (e.g. R1.005 → R1.01), the standard South African retail rule.
 * Rounding only happens here at display time.
 *
 * Locale policy:
 * Locale.ENGLISH is used for all number formatting so that the
 * decimal separator is always a dot (.) and the thousands separator
 * is always a comma (,) — regardless of the computer's language
 * settings. This keeps receipt output consistent everywhere.
 */
public class OrderFormatter {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    /**
     * Formats the full invoice receipt for an order as a String.
     *
     * @param order The order to format. Must not be null.
     * @return A multi-line receipt string ready for printing or testing.
     */
    public String format(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot format a null order.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("==================================================\n");
        sb.append("               PAYNEST INVOICE\n");
        sb.append("==================================================\n");
        sb.append(String.format(Locale.ENGLISH, "Order ID  : %d%n", order.getId()));
        sb.append(String.format(Locale.ENGLISH, "Customer  : %s%n", order.getCustomer().getName()));
        sb.append(String.format(Locale.ENGLISH, "Email     : %s%n", order.getCustomer().getEmail()));
        sb.append("--------------------------------------------------\n");
        sb.append(String.format(Locale.ENGLISH, "%-22s %-6s %s%n", "Product", "Qty", "Subtotal"));
        sb.append("--------------------------------------------------\n");

        for (OrderItem item : order.getItems()) {
            BigDecimal lineTotal = item.calculateTotal().setScale(SCALE, ROUNDING);
            sb.append(String.format(Locale.ENGLISH, "%-22s %-6d R%,.2f%n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    lineTotal));
        }

        BigDecimal grandTotal = order.calculateTotal().setScale(SCALE, ROUNDING);
        sb.append("--------------------------------------------------\n");
        sb.append(String.format(Locale.ENGLISH, "%-29s R%,.2f%n", "TOTAL:", grandTotal));
        sb.append("==================================================\n");

        return sb.toString();
    }
}