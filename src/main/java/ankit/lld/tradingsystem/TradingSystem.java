package ankit.lld.tradingsystem;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class TradingSystem {

    public static void main(String[] args) {
        TradingSystem tradingSystem = new TradingSystem();
        Order order1 = tradingSystem.placeOrder(1l, SYMBOL_UBER, OrderType.BUY, 10, 100, 0);
        System.out.println("Order 1 Status " + order1.status.name());

        Order order2 = tradingSystem.placeOrder(2l, SYMBOL_UBER, OrderType.BUY, 20, 90, 0);
        System.out.println("Order 2 Status " + order2.status.name());

        Order order3 = tradingSystem.placeOrder(3l, SYMBOL_UBER, OrderType.SELL, 30, 90, 0);

        System.out.println("Order 3 Status " + order3.status.name());
        safeSleep(1000);

        System.out.println("Order 1 Status " + order1.status.name());
        System.out.println("Order 2 Status " + order2.status.name());
        System.out.println("Order 3 Status " + order3.status.name());

        Order order4 = tradingSystem.placeOrder(1l, SYMBOL_UBER, OrderType.SELL, 30, 90, 5);
        System.out.println("Order 4 Status " + order4.status.name());
        safeSleep(10000);
        System.out.println("Order 4 Status " + order4.status.name());


    }

    private static final String SYMBOL_UBER = "UBER";
    private static final String SYMBOL_CRM = "CRM";
    private final Map<String, OrderBook> symbolOrderBookMap;
    private final Map<Long, Order> allOrderMap;
    private final AtomicLong orderIdGenerator = new AtomicLong(0);
    private final AtomicLong userIdGenerator = new AtomicLong(0);
    private final Map<Long, User> users;

    // For order cancellation after fixed time.
    private final ScheduledExecutorService scheduledExecutorService;

    public TradingSystem(){
        this.symbolOrderBookMap = new HashMap<>();
        this.allOrderMap = new HashMap<>();
        this.symbolOrderBookMap.put(SYMBOL_CRM, new OrderBook(SYMBOL_CRM));
        this.symbolOrderBookMap.put(SYMBOL_UBER, new OrderBook(SYMBOL_UBER));

        this.users = new HashMap<>();
        long userId = userIdGenerator.incrementAndGet();
        this.users.put(userId, new User(userId, "Ankit")); // 1
        userId = userIdGenerator.incrementAndGet();
        this.users.put(userId, new User(userId, "A Kumar"));  //2
        userId = userIdGenerator.incrementAndGet();
        this.users.put(userId, new User(userId, "Kumar"));  // 3

        this.scheduledExecutorService = Executors.newScheduledThreadPool(10);
    }

    // API -1: Place order
    public Order placeOrder(long userId, String symbol, OrderType orderType, int quantity, double price, int expirySeconds){
        OrderBook orderBook = this.symbolOrderBookMap.get(symbol);
        if(orderBook == null){
            orderBook = new OrderBook(symbol);
            this.symbolOrderBookMap.put(symbol, orderBook);
        }
        Order order = new Order(orderIdGenerator.incrementAndGet(), userId, symbol, orderType,quantity,price );
        orderBook.addOrder(order);
        this.allOrderMap.put(order.id, order);

        if(expirySeconds > 0){
            scheduledExecutorService.schedule(() -> cancelOrder(order.id), expirySeconds, TimeUnit.SECONDS);
        }
        return order;
    }

    // API - 2: Get order details.

    public Order getOrderDetails(long orderId){
        return this.allOrderMap.get(orderId);
    }

    // API - 3 : Get Trades for symbol
    public List<Trade> getTrades(String symbol){
        return this.symbolOrderBookMap.get(symbol).getTrades();
    }

    // API - 3: Cancel Order
    public Order cancelOrder(long orderId){
        Order order = getOrderDetails(orderId);
        OrderBook orderBook = this.symbolOrderBookMap.get(order.symbol);
        boolean cancelStatus = orderBook.cancelOrder(order);
        if(!cancelStatus){
            System.out.println("Order already executed!");
        }else{
            System.out.println("Order cancelled successfully!");
        }
        return order;
    }

    // API - 4: Modify Order

    private static void safeSleep(long millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){

        }

    }

}

class User{
    long id;
    String name;

    public User(long id, String name){
        this.id = id;
        this.name = name;
    }
}

class Order{
    long id;
    long userId;
    String symbol;
    OrderType orderType;
    int quantity;
    double price;
    long timeStamp;
    OrderStatus status;

    public Order(long id, long userId, String symbol, OrderType orderType, int quantity, double price){
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.timeStamp = Instant.now().toEpochMilli();
        this.status = OrderStatus.OPEN;
    }
}

class Trade{
    long tradeId;
    long buyOrderId;
    long sellOrderId;
    String symbol;
    int quantity;
    double price;
    long timeStamp;

    public Trade(long tradeId, String symbol, long buyOrderId, long sellOrderId, int quantity, double price) {
        this.tradeId = tradeId;
        this.symbol = symbol;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.quantity = quantity;
        this.price = price;
        this.timeStamp = Instant.now().toEpochMilli();
    }
}

class OrderBook{
    long id;
    final String symbol;
    private final TreeSet<Order> buyOrders;
    private final TreeSet<Order> sellOrders;
    private final List<Trade> trades;
    private final AtomicLong tradeIdGenerator = new AtomicLong(0);
    private final ReentrantLock lock = new ReentrantLock();

    public OrderBook(String symbol){
        this.symbol = symbol;
        this.buyOrders = new TreeSet<>((o1, o2) -> {
           if(o1.price != o2.price){
               return Double.compare(o2.price, o1.price);
           }else if(o1.timeStamp != o2.timeStamp){
               return Long.compare(o1.timeStamp, o2.timeStamp);
           }
           return Long.compare(o1.id, o2.id);
        });

        this.sellOrders = new TreeSet<>((o1, o2) -> {
            if(o1.price != o2.price){
                return Double.compare(o1.price, o2.price);
            }else if(o1.timeStamp != o2.timeStamp){
                return Long.compare(o1.timeStamp, o2.timeStamp);
            }
            return Long.compare(o1.id, o2.id);
        });

        this.trades = new ArrayList<>();
    }

    public void addOrder(Order order){
        lock.lock();
        try{
            if(order.orderType == OrderType.BUY){
                this.buyOrders.add(order);
            }else{
                this.sellOrders.add(order);
            }
            System.out.println("Your order with id: "+id+" placed successfully!");
            executeOrder();
        }finally {
            lock.unlock();
        }
    }

    private void executeOrder(){
        while(!buyOrders.isEmpty() && !sellOrders.isEmpty()){
            Order buyOrder = buyOrders.first();
            Order sellOrder = sellOrders.first();
            if(buyOrder.price < sellOrder.price){
                break;
            }
            int tradedQuantity = Math.min(buyOrder.quantity, sellOrder.quantity);
            double tradePrice = sellOrder.price;
            Trade trade = new Trade(tradeIdGenerator.incrementAndGet(), symbol, buyOrder.id, sellOrder.id, tradedQuantity, tradePrice);
            this.trades.add(trade);

            buyOrder.quantity = buyOrder.quantity - tradedQuantity;
            if(buyOrder.quantity == 0){
                buyOrder.status = OrderStatus.COMPLETED;
                buyOrders.removeFirst();
            }else{
                buyOrder.status = OrderStatus.PARTIALLY_COMPLETED;
            }

            sellOrder.quantity = sellOrder.quantity - tradedQuantity;
            if(sellOrder.quantity == 0){
                sellOrder.status = OrderStatus.COMPLETED;
                sellOrders.removeFirst();
            }else{
                sellOrder.status = OrderStatus.PARTIALLY_COMPLETED;
            }
        }
    }

    public boolean cancelOrder(Order order){
        lock.lock();
        try{
            if(order.orderType == OrderType.SELL){
                if(this.sellOrders.contains(order)){
                    order.status = OrderStatus.CANCELLED;
                }else{
                    return false;
                }
                return this.sellOrders.remove(order);
            }else{
                if(this.buyOrders.contains(order)){
                    order.status = OrderStatus.CANCELLED;
                }else{
                    return false;
                }
                return this.buyOrders.remove(order);
            }
        }finally{
            lock.unlock();
        }
    }

    public List<Trade> getTrades(){
        return this.trades;
    }

}

enum OrderType{
    BUY, SELL;
}

enum OrderStatus{
    OPEN, COMPLETED, PARTIALLY_COMPLETED, CANCELLED
}
