package ankit.dsa;

import ankit.lld.binpackaging.BinPackaging;
import ankit.lld.binpackaging.BinPackagingResponse;
import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;
import ankit.lld.binpackaging.strategy.DefaultPackagingStrategy;
import ankit.lld.binpackaging.strategy.OptimalPackagingStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinPackagingTest {

    @Test
    public void testForSingleBoxPlacement(){
        List<Truck> trucks = new ArrayList<>(List.of(new Truck(1, 5, 6, 4),
                new Truck(2, 4, 4, 4)));
        List<Box> boxes = new ArrayList<>(List.of(new Box(1, 2, 2, 2),
                new Box(2, 2, 1, 2),
                new Box(2, 5, 2, 2),
                new Box(2, 4, 5, 3)));
        BinPackaging binPackaging = new BinPackaging(new DefaultPackagingStrategy());
        BinPackagingResponse response = binPackaging.placeBoxesInTruck(trucks, boxes);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(1, response.getPlacedMap().size());
    }

    @Test
    public void testForMultipleBoxPlacement(){
        List<Truck> trucks = new ArrayList<>(List.of(new Truck(1, 5, 6, 4),
                new Truck(2, 4, 4, 4)));
        List<Box> boxes = new ArrayList<>(List.of(new Box(1, 2, 2, 2),
                new Box(2, 2, 1, 2),
                new Box(2, 5, 2, 2),
                new Box(2, 4, 5, 3),
                new Box(2, 4, 5, 2)));
        BinPackaging binPackaging = new BinPackaging(new DefaultPackagingStrategy());
        BinPackagingResponse response = binPackaging.placeBoxesInTruck(trucks, boxes);
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(2, response.getPlacedMap().size());

    }

    @Test
    public void testForNoAllBoxPlacement(){
        List<Truck> trucks = new ArrayList<>(List.of(new Truck(1, 5, 6, 4),
                new Truck(2, 4, 4, 4)));
        List<Box> boxes = new ArrayList<>(List.of(new Box(1, 2, 2, 2),
                new Box(2, 2, 1, 2),
                new Box(2, 5, 2, 2),
                new Box(2, 4, 5, 3),
                new Box(2, 8, 8, 1)));
        BinPackaging binPackaging = new BinPackaging(new DefaultPackagingStrategy());
        BinPackagingResponse response = binPackaging.placeBoxesInTruck(trucks, boxes);
        assertEquals("FAIL", response.getStatus());
    }

    @Test
    public void testForNoAllBoxPlacementWithOptimalStrategy(){
        List<Truck> trucks = new ArrayList<>(List.of(new Truck(1, 5, 6, 4),
                new Truck(2, 4, 4, 4)));
        List<Box> boxes = new ArrayList<>(List.of(new Box(1, 2, 2, 2),
                new Box(2, 2, 1, 2),
                new Box(2, 5, 2, 2),
                new Box(2, 4, 5, 3),
                new Box(2, 8, 8, 1)));
        BinPackaging binPackaging = new BinPackaging(new OptimalPackagingStrategy());
        BinPackagingResponse response = binPackaging.placeBoxesInTruck(trucks, boxes);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals(2, response.getPlacedMap().size());
    }
}
