package ankit.lld;

import ankit.lld.binpackaging.fleetpackaging.model.Container;
import ankit.lld.binpackaging.fleetpackaging.model.Order;
import ankit.lld.binpackaging.fleetpackaging.model.OrderItem;
import ankit.lld.binpackaging.fleetpackaging.model.Product;
import ankit.lld.binpackaging.fleetpackaging.service.PackagingService;
import ankit.lld.binpackaging.fleetpackaging.strategy.SmallestContainerSelectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PackagingServiceTest {

    private List<Container> containers;
    private List<Product> products;

    @BeforeEach
    public void init(){
        containers = new ArrayList<>();
        products = new ArrayList<>();
        containers.add(new Container(3, 10, 20, 30));
        containers.add(new Container(1, 50, 60, 70));
        containers.add(new Container(3, 100, 200, 300));

        products.add(new Product(1, 2, 4, 10));
        products.add(new Product(2, 10, 30, 4));
        products.add(new Product(3, 5, 6, 7));
    }

    @Test
    public void testOrderCanBeKeptInContainer(){
        var orderItems = List.of(new OrderItem(products.getFirst(), 3),
                new OrderItem(products.get(2), 7));
        var order = new Order(1, orderItems);
        PackagingService service = new PackagingService(new SmallestContainerSelectionStrategy());
        System.out.println(service.getContainerPlacement(containers, new ArrayList<>(List.of(order))));
        System.out.println(service.getMaxOrderPerContainer(containers, new ArrayList<>(List.of(order))));
    }

}
