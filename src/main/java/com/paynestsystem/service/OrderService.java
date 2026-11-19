package com.paynestsystem.service;

import java.math.BigDecimal;

import com.paynestsystem.domain.Order;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;

/**
 * Orchestrates order-related operations such as printing receipts
 * and checking out with a chosen payment method.
 *
 * OrderService asks OrderFormatter to build the text, then prints it.
 * If we later want to email the receipt, we change only this class.
 */
public class OrderService {

    private final OrderFormatter formatter;
    private final PaymentProcessor paymentProcessor;

    public OrderService() {
        this.formatter = new OrderFormatter();
        this.paymentProcessor = new PaymentProcessor();
    }

    /**
     * Prints the order receipt to the console.
     * @param order The order to print. Must not be null.
     */
    public void printSummary(Order order) {
        if (order == null) {
            System.out.println("No order details available.");
            return;
        }
        System.out.print(formatter.format(order));
    }

    /**
     * Checks out an order with the given payment method.
     * Returns true only if the payment went through. An order whose total
     * has fractions of a cent is refused, because it cannot be charged exactly.
     */
    public boolean checkout(Order order, PaymentMethod method) {
        if (order == null) {
            System.out.println("No order details available.");
            return false;
        }
        if (method == null) {
            System.out.println("No payment method provided.");
            return false;
        }

        BigDecimal total = order.calculateTotal();

        if (total.stripTrailingZeros().scale() > 2) {
            System.out.println("Order " + order.getId() + " not completed: total of R"
                    + total.toPlainString() + " has fractions of a cent");
            return false;
        }

        // The only place the exact total becomes a double, because PaymentMethod takes a double
        double amount = total.doubleValue();
        boolean success = paymentProcessor.processPayment(method, amount);

        if (success) {
            System.out.println("Order " + order.getId() + " completed.");
        } else {
            System.out.println("Order " + order.getId() + " not completed.");
        }

        return success;
    }
}