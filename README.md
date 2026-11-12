## Running the demo

You need Java 17 or newer and Apache Maven 3.6+.

From the project root (the folder containing `pom.xml`), run:

```bash
mvn compile exec:java
```

The demo builds the order, prints the invoice, then checks out with a card payment. The end of the output looks like this:

```
Processing card payment for R4798.29
Payment successful via CARD
Amount: R4798.29
Order 5001 completed.
```

To try another rail, change the payment line in `PayNestApplication.java` to `new EftPayment()` or `new WalletPayment(10000)`. The wallet needs an opening balance that covers the order total. Nothing else in the checkout changes.

## Running the tests

```bash
mvn test
```

The tests cover all three rails, rejected amounts, a wallet decline travelling back through checkout, and that the amount charged equals the order total.

## Design notes: payment abstraction

Checkout is built against the PaymentMethod interface instead of one method with a branch for each payment type. CardPayment, EftPayment and WalletPayment each implement the interface and keep their own console messages and rules. OrderService.checkout and PaymentProcessor depend only on the PaymentMethod contract, so adding a rail such as BuyNowPayLaterPayment means writing one new class and wiring it in the demo, with no change to the order total calculation. A single large method using if/else or switch statements would have to be edited for every new rail, which risks breaking rails that already work.

Rules sit at two levels. The amount check (the amount must be greater than R0) is written once as a default method on PaymentMethod, so every rail inherits it. The wallet adds a rule of its own, a balance check, because only it needs one. A rail answers a request with true or false, and a decline is a normal answer and not an error, so checkout simply reports that the order was not completed.

## Class diagram

```
            <<interface>>
             PaymentMethod
   +processPayment(amount): boolean
   +getPaymentType(): String
   +isValidAmount(amount): boolean
                 ^
                 | implements
      +----------+----------+
      |          |          |
CardPayment  EftPayment  WalletPayment
                         -balance: double

PaymentProcessor
   +processPayment(method: PaymentMethod, amount: double): boolean
```