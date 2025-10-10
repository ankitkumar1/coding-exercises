package ankit.lld.binpackaging.fleetpackaging.strategy;

import ankit.lld.binpackaging.fleetpackaging.model.Container;
import ankit.lld.binpackaging.fleetpackaging.model.Order;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SmallestContainerSelectionStrategy implements ContainerSelectionStrategy{

    @Override
    public Optional<Container> findBestFit(List<Container> containers, Order order) {
        double required = order.getVolume();
        return containers.stream()
                    .filter(container -> container.getVolume() >= required)
                    .min(Comparator.comparingDouble(Container::getVolume));
    }
}
