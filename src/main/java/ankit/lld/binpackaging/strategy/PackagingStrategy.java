package ankit.lld.binpackaging.strategy;

import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;

import java.util.List;
import java.util.Map;

public interface PackagingStrategy {
    Map<Truck, List<Box>> placeBoxes(List<Truck> trucks, List<Box> boxes);
}
