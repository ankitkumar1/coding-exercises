package ankit.lld.binpackaging.fleetpackaging.model;

public class Container {

    private final int containerId;
    private final double length;
    private final double width;
    private final double height;
    private final double volume;

    public Container(int containerId, double length, double width, double height) {
        this.containerId = containerId;
        this.length = length;
        this.width = width;
        this.height = height;
        this.volume = length * width * height;
    }

    public int getContainerId() {
        return containerId;
    }

    public double getVolume() {
        return volume;
    }
}
