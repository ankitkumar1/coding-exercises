package ankit.lld.binpackaging.fleetpackaging.service;

import ankit.lld.binpackaging.fleetpackaging.model.Container;
import ankit.lld.binpackaging.fleetpackaging.model.Order;
import ankit.lld.binpackaging.fleetpackaging.strategy.ContainerSelectionStrategy;

import java.util.*;

public class PackagingService {

    private final ContainerSelectionStrategy strategy;

    public PackagingService(ContainerSelectionStrategy strategy){
        this.strategy = strategy;
    }

    public Map<Integer, Integer> getContainerPlacement(List<Container> containers, List<Order> orders){
        var placementMap = new HashMap<Integer, Integer>();
        for(Order order : orders){
            Optional<Container> container = strategy.findBestFit(containers, order);
            container.ifPresent(container1 ->
                    placementMap.put(container1.getContainerId(), order.getOrderId()));
        }
        return placementMap;
    }

    public Map<Integer, Integer> getMaxOrderPerContainer(List<Container> containers, List<Order> orders){
        var containerWithOrderCount = new HashMap<Integer, Integer>();
        orders.sort(Comparator.comparing(Order::getVolume).reversed());
        for(Container container : containers){
            containerWithOrderCount.put(container.getContainerId(), 0);
            double availableVolume = container.getVolume();
            int placedOrder = 0;
            for(Order order : orders){
                if(order.getVolume() > availableVolume) {
                    break;
                }
                placedOrder++;
                availableVolume -= order.getVolume();
            }
            containerWithOrderCount.put(container.getContainerId(), placedOrder);
        }
        return containerWithOrderCount;
    }
}
