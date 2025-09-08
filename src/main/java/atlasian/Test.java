package atlasian;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class Test {
    public static void main(String[] args) {
        TreeSet<Node> treeSet = new TreeSet<>((n1, n2) -> {
            if(n1.val == n2.val){
                return 1;
            }
            return Integer.compare(n1.val, n2.val);
        });

        PriorityQueue<Node> heap = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));

        Node node1 = new Node(10, "Node - 1");
        Node node2 = new Node(10, "Node - 2");

        treeSet.add(node1);
        treeSet.add(node2);

        heap.add(node1);
        heap.add(node2);

        System.out.println(treeSet);
        System.out.println(heap);
    }

    static class Node{
        int val;
        String name;
        Node(int val, String name){
            this.val = val;
            this.name = name;
        }

        @Override
        public String toString(){
            return this.name;
        }
    }
}
