package com.paynestsystem.app;

import com.paynestsystem.domain.Customer;
import com.paynestsystem.domain.Order;
import com.paynestsystem.domain.Product;
import com.paynestsystem.payment.CardPayment;
import com.paynestsystem.payment.PaymentMethod;
import com.paynestsystem.service.OrderService;

public class PayNestApplication {

    public static void main(String[] args) {

        // Create sample products
        Product womack = new Product(101, "The Poet - Bobby Womack", "849.99");
        Product queen  = new Product(102, "Hot Space - Queen",       "999.90");
        Product miles  = new Product(103, "Kind of Blue - Miles Davis", "649.50");

        // Create a sample customer
        Customer customer = new Customer(1, "Mia Mzimela", "mia.mzimela@umuzi.org");

        // Create an order for that customer
        Order order = new Order(5001, customer);

        // Add items — one line has quantity > 1 to demo multi-unit totals
        order.addItem(womack, 1);  // 1 x R849.99  = R849.99
        order.addItem(queen,  2);  // 2 x R999.90  = R1,999.80
        order.addItem(miles,  3);  // 3 x R649.50  = R1,948.50
        // Grand total: R4,798.29

        // Print the invoice
        OrderService orderService = new OrderService();
        orderService.printSummary(order);

        // Check out the order using a card payment.
        // Swap this line for new EftPayment() or new WalletPayment()
        // to demonstrate the same checkout flow working with a
        // different rail — no other code needs to change.
        PaymentMethod paymentMethod = new CardPayment();
        orderService.checkout(order, paymentMethod);
    }
}