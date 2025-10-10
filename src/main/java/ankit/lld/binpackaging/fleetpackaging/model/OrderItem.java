package ankit.lld.binpackaging.fleetpackaging.model;

public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getVolume(){
        return this.product.getVolume() * this.quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void increaseQuantity(int delta) {
        this.quantity += delta;
    }
}
