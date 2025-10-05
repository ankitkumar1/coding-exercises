package ankit.lld.binpackaging.strategy;


import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;

import java.util.*;

public class DefaultPackagingStrategy implements PackagingStrategy {

    @Override
    public Map<Truck, List<Box>> placeBoxes(List<Truck> trucks, List<Box> boxes) {
        trucks.sort(Comparator.comparing(Truck::getVolume).reversed());
        boxes.sort(Comparator.comparing(Box::getVolume).reversed());

        Map<Truck, List<Box>> placedBoxMap = new LinkedHashMap<>();
        int i = 0;
        for(Truck truck : trucks){
            while(i<boxes.size()){
                Box box = boxes.get(i);
                if(!truck.canPlace(box)){
                    break;
                }
                truck.place(box);
                placedBoxMap.computeIfAbsent(truck, k -> new ArrayList<>()).add(box);
                i++;
            }
        }
        return i==boxes.size() ? placedBoxMap : Collections.emptyMap();
    }
}
