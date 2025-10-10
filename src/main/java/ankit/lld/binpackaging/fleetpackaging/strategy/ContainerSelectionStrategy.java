package ankit.lld.binpackaging.fleetpackaging.strategy;

import ankit.lld.binpackaging.fleetpackaging.model.Container;
import ankit.lld.binpackaging.fleetpackaging.model.Order;

import java.util.List;
import java.util.Optional;

public interface ContainerSelectionStrategy {
    Optional<Container> findBestFit(List<Container> containers, Order order);
}
