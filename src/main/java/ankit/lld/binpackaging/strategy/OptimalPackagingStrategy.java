package ankit.lld.binpackaging.strategy;


import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;

import java.util.*;

public class OptimalPackagingStrategy implements PackagingStrategy {

    @Override
    public Map<Truck, List<Box>> placeBoxes(List<Truck> trucks, List<Box> boxes) {
        trucks.sort(Comparator.comparing(Truck::getVolume).reversed());
        boxes.sort(Comparator.comparing(Box::getVolume).reversed());

        Map<Truck, List<Box>> placedBoxMap = new LinkedHashMap<>();
        for(Truck truck : trucks){
            if(boxes.isEmpty()){
                break;
            }
            ListIterator<Box> listIterator = boxes.listIterator();
            while(listIterator.hasNext()){
                Box box = listIterator.next();
                if(!truck.canPlace(box)){
                    continue;
                }
                listIterator.remove();
                truck.place(box);
                placedBoxMap.computeIfAbsent(truck, k -> new ArrayList<>()).add(box);
            }
        }
        return boxes.isEmpty() ? placedBoxMap : Collections.emptyMap();
    }
}
