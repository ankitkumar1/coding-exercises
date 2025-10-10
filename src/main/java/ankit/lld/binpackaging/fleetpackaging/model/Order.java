package ankit.lld.binpackaging.fleetpackaging.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Order {
    private int orderId;
    private Map<Integer, OrderItem> items;
    private double volume;

    public Order(int orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items.stream().collect(Collectors.toMap(item -> item.getProduct().getProductId(), item -> item));
        this.volume = items.stream().mapToDouble(OrderItem::getVolume).sum();
    }

    public void addProduct(Product product, int quantity){
        OrderItem orderItem = this.items.get(product.getProductId());
        orderItem.increaseQuantity(quantity);
        this.volume += quantity * product.getVolume();
    }

    public double getVolume(){
        return this.volume;
    }

    public int getOrderId(){
        return this.orderId;
    }
}
