Design and implement an efficient in-memory trading system similar to a stock exchange, where registered users can place, execute and cancel trades. The system should demonstrate synchronization and concurrency in a multi-threaded environment. Your system should be able to perform the following items:

Functional Requirements:
Your system should support the following functionalities:

A registered user can place, modify, and cancel his orders.
A user should be able to query the status of his order
[IMPORTANT] - The system should be able to execute trades based on matching buy and sell orders. A trade is executed when the buy and sell price of two different orders match/are equal. If multiple eligible orders can be matched with the same price, match the oldest orders first.
[IMPORTANT] - Concurrent order placement, modification, cancellation, and execution should be handled appropriately.
The system should maintain an order book per symbol, which holds all the current unexecuted orders. Your system should store at least the following mentioned details.
User details
    User ID
    User Name
    Phone Number
    Email Id
Orders
    Order ID
    User ID
    OrderType (Buy/Sell)
    Stock Symbol (eg: RELIANCE, WIPRO etc.)
    Quantity
    Price
    Order Accepted Timestamp
    Status (ACCEPTED, REJECTED, CANCELED)
Trades
    Trade ID
    Trade Type (Buy/Sell)
    Buyer Order Id
    Seller Order Id
    Stock Symbol
    Quantity
    Price
    Trade Timestamp

Additional functionality, but not compulsory [implement if time permits]:
Implement order expiry. A order should be automatically canceled if that order is not executed within a specific time.

Expectation:
    Your code should be executable (at worst partial running would work) & should be clean.
    Your code should be adequately refactored, and exceptions should be gracefully handled.
    Your code should store all the attributes explained under the "Stores" section.
    Your code should cover all the functionality in the "Supports" section.
If you get extra time, you can code for " Additional functionality, but not compulsory." This will get you extra credit.
Guidelines:

You don't have to build the user registration part. Consider some dummy users registered and use those in the entire system.
You should use the in-memory data structure of your preferred language to store the data but have the right abstractions so that other persistent stores can be plugged in.
Evaluation criteria:
    Executable code.
    Code readability and testability
    Refactored code
    Abstraction
    Object-Oriented concepts.
    Language proficiency.
============================================
Design Patterns
1. Command Pattern (lightweight flavor)
    Where: Order objects themselves.
    Why:
        An Order encapsulates an action request (BUY 100 RELIANCE @ 2500) along with metadata (who, when).
        The OrderBook executes those commands later when matches occur.
        This is similar to Command Pattern: an object representing an operation with all needed data.
2. Mediator Pattern
    Where: OrderBook.
    Why:
      Acts as a mediator between different orders (buyers/sellers).
      Instead of orders talking to each other, the OrderBook decides who gets matched, in what order, and at what price.
      Centralizes communication logic → prevents orders from being tightly coupled.
3. Singleton: TradingSystem object.
4. Decorator:
   We didn’t bake expiry inside OrderBook → instead we decorated TradingSystem with expiry scheduling.

SOLID Principle
=============================
🔹 S — Single Responsibility Principle (SRP)
    👉 A class should have only one reason to change.
    Order → only represents an order’s data (not matching logic).
    Trade → represents execution details.
    OrderBook → only manages order queues and matching.
    TradingSystem → orchestrates across multiple order books, ID generation, and expiry scheduling.
    TradingEventPublisher → only handles event broadcasting.
🔹 O — Open/Closed Principle (OCP)
    Comparators (Strategy Pattern) for buy/sell → 
            if exchange rules change (e.g., pro-rata matching, auction mode), 
            we add new comparators instead of rewriting OrderBook.
🔹 L — Liskov Substitution Principle (LSP)
    Currently we have Order and Trade as final entities.
    If tomorrow we introduce MarketOrder extends Order or StopLossOrder extends Order, they 
    should still work with OrderBook because OrderBook only depends on the abstract Order properties (price, quantity, timestamp).

