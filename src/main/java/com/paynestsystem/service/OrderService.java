package com.paynestsystem.service;

import com.paynestsystem.domain.Order;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.payment.PaymentProcessor;

/**
 * Orchestrates order-related operations such as printing receipts
 * and processing checkout payments.
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
     * Checks out an order using the supplied payment method.
     * Calculates the order total once and passes it to the chosen
     * PaymentMethod — this method never checks which rail it's
     * talking to, so new payment types require no changes here.
     *
     * @param order  The order to check out. Must not be null.
     * @param method The payment rail to use. Must not be null.
     * @return true if the payment succeeded, false otherwise.
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

        double amount = order.calculateTotal().doubleValue();
        boolean success = paymentProcessor.processPayment(method, amount);

        if (success) {
            System.out.println("Order " + order.getId() + " completed.");
        }

        return success;
    }
}