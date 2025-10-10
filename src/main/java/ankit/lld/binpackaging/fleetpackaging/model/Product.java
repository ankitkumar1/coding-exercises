package ankit.lld.binpackaging.fleetpackaging.model;

public class Product {
    private final int productId;
    private final double length;
    private final double width;
    private final double height;
    private final double volume;

    public Product(int productId, double length, double width, double height) {
        this.productId = productId;
        this.length = length;
        this.width = width;
        this.height = height;
        this.volume = this.length * this.width * this.height;
    }

    public int getProductId() {
        return productId;
    }

    public double getVolume() {
        return volume;
    }
}
