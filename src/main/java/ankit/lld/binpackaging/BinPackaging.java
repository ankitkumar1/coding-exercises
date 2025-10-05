package ankit.lld.binpackaging;

import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;
import ankit.lld.binpackaging.strategy.PackagingStrategy;

import java.util.*;

public class BinPackaging {
    PackagingStrategy strategy;
    public BinPackaging(PackagingStrategy strategy){
        this.strategy = strategy;
    }

    public BinPackagingResponse placeBoxesInTruck(List<Truck> trucks, List<Box> boxes){
        Map<Truck, List<Box>> map = strategy.placeBoxes(trucks, boxes);
        if(map.isEmpty()){
            return new BinPackagingResponse.Builder().status("FAIL").build();
        }
        return new BinPackagingResponse.Builder().status("SUCCESS").placedMap(map).build();
    }
}

