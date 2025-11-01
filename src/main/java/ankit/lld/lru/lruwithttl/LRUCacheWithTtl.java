package ankit.lld.lru.lruwithttl;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheWithTtl {

    private final int capacity;
    private final Map<Integer, Node> map;
    private final DoublyLinkedList linkedList;
    private long runningValueSum;

    public LRUCacheWithTtl(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.linkedList = new DoublyLinkedList();
    }

    public int get(int key){
        Node node = this.map.get(key);
        if(node == null || node.isExpired(System.currentTimeMillis())){
            removeEntry(node);
            return -1;
        }
        linkedList.moveToHead(node);
        return node.value;
    }

    public void put(int key, int value, int ttlInSeconds){
        Node node = map.get(key);
        if(node != null){
            runningValueSum -= node.value;
            runningValueSum += value;
            node.value = value;
            node.expiryTimestamp = ttlInSeconds == -1 ? -1 :
                        System.currentTimeMillis() + (ttlInSeconds * 1000L);
            linkedList.moveToHead(node);
            return;
        }
        if(map.size() == capacity){
            evictExpired();
            if(map.size() == capacity){
                node = linkedList.removeLast();
                map.remove(node.key);
                runningValueSum -= node.value;
            }
        }
        // Create New
        node = new Node(key, value, -1);
        node.expiryTimestamp = ttlInSeconds == -1 ? -1 :
                System.currentTimeMillis() + (ttlInSeconds * 1000L);
        linkedList.addToHead(node);
        map.put(key, node);
    }

    public double getAverage(){
        evictExpired();
        return map.isEmpty() ? 0.0d : ((double)runningValueSum / map.size());
    }

    private void evictExpired() {
        Node last = linkedList.tail.prev;
        long now = System.currentTimeMillis();
        while(last!=linkedList.head && last.isExpired(now)){
            removeEntry(last);
            last = last.prev;
        }
    }

    private void removeEntry(Node node){
        if(node == null){
            return;
        }
        runningValueSum -= node.value;
        linkedList.remove(node);
        map.remove(node.key);
    }


    static class DoublyLinkedList{
        private final Node head;
        private final Node tail;

        DoublyLinkedList(){
            this.head = new Node();
            this.tail = new Node();
            head.next = tail;
            tail.prev = head;
        }

        public void addToHead(Node node){
            node.next = head.next;
            node.prev = head;
            head.next = node;
            node.next.prev = node;
        }

        public Node removeLast(){
            Node last = tail.prev;
            if(last == head){
                return null;
            }
            tail.prev = last.prev;
            last.prev.next = tail;
            return last;
        }

        public void remove(Node node){
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
        }

        public void moveToHead(Node node){
            remove(node);
            addToHead(node);
        }
    }

    static class Node{
        int key, value;
        Node next, prev;
        long expiryTimestamp;

        Node(){

        }
        Node(int key, int value, long expiryTimestamp){
            this.key = key;
            this.value = value;
            this.expiryTimestamp = expiryTimestamp;
        }

        public boolean isExpired(long now){
            return expiryTimestamp != -1 &&  expiryTimestamp <= now;
        }

    }
}
