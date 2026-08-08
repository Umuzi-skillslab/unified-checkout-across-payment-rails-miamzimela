[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=24291578&assignment_repo_type=AssignmentRepo)

## Design Notes: Payment Abstraction

Checkout is implemented against a `PaymentMethod` interface rather than a single
method containing branching logic for each payment type. Each payment rail
(`CardPayment`, `EftPayment`, `WalletPayment`) implements this interface
independently, encapsulating its own processing behaviour and console output.
`OrderService.checkout()` and `PaymentProcessor` depend only on the
`PaymentMethod` contract, not on any concrete rail — meaning the order total
calculation and checkout orchestration require no changes when a new payment
type is introduced. A single mega-method using if/else or switch statements
would require every new rail to modify shared logic directly, increasing the
risk of regressions in code paths unrelated to the new feature. This design
keeps `Order`'s core arithmetic untouched and isolates rail-specific behaviour
to a single new class per addition (for example, `BuyNowPayLaterPayment` would
require only a new class implementing `PaymentMethod`, plus one line of
wiring in the composition root).

### Class Diagram

```
        <<interface>>
         PaymentMethod
    +processPayment(amount): boolean
    +getPaymentType(): String
              ▲
              │ implements
    ┌─────────┼─────────┐
    │         │         │
CardPayment EftPayment WalletPayment

PaymentProcessor
    +processPayment(method: PaymentMethod, amount: double): boolean
```