package ankit.lld.binpackaging.strategy;


import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;

import java.util.*;

public class OptimalPackagingStrategy implements PackagingStrategy {

    @Override
    public Map<Truck, List<Box>> placeBoxes(List<Truck> trucks, List<Box> boxes) {
        trucks.sort(Comparator.comparing(Truck::getVolume).reversed());
        boxes.sort(Comparator.comparing(Box::getVolume).reversed());

        BoxLL linkedList = new BoxLL();
        linkedList.addAll(boxes);

        Map<Truck, List<Box>> placedBoxMap = new LinkedHashMap<>();

        for(Truck truck : trucks){
            if(linkedList.isEmpty()){
                break;
            }
            Node current = linkedList.getRoot();
            while(current != null && current.next != null){
                Box box = current.next.box;
                if(!truck.canPlace(box)){
                    current = current.next;
                    continue;
                }
                truck.place(box);
                placedBoxMap.computeIfAbsent(truck, k -> new ArrayList<>()).add(box);
                linkedList.removeNext(current);
            }
        }
        return linkedList.isEmpty() ? placedBoxMap : Collections.emptyMap();
    }

    class BoxLL{
        Node root;
        BoxLL(){
            this.root = new Node(null);
        }

        public void addAll(List<Box> boxes){
            Node current = root;
            for(Box box : boxes){
                current.next = new Node(box);
                current = current.next;
            }
        }

        public void removeNext(Node node){
            if(node.next ==null){
                throw new IllegalArgumentException("You can't remove last node");
            }
            node.next = node.next.next;
        }

        public Node getRoot(){
            return this.root;
        }

        public boolean isEmpty(){
            return root.next == null;
        }

        public Node getNext(Node node){
            if(node.next ==null){
                throw new IllegalArgumentException("You can't remove last node");
            }
            return node.next;
        }
    }

    class Node{
        Box box;
        Node next;
        Node(Box box){
            this.box=box;
        }
    }
}
