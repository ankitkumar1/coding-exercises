package ankit.lld.lru.lruwithttl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCacheWithTtlGeneric<K, V extends Number> {

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> linkedList;
    private Double runningValueSum = 0d;
    private final ReadWriteLock lock;

    public LRUCacheWithTtlGeneric(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.linkedList = new DoublyLinkedList<>();
        this.lock = new ReentrantReadWriteLock();

        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "LRUCacheCleaner");
            t.setDaemon(true);
            return t;
        });
        cleaner.scheduleAtFixedRate(this::evictExpired, 100, 1, TimeUnit.MINUTES);
    }

    public V get(K key){
        lock.readLock().lock();

        try{
            var node = this.map.get(key);
            if(node == null || node.isExpired(System.currentTimeMillis())){
                removeEntry(node);
                return null;
            }
        }finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try{
            var node = this.map.get(key);
            linkedList.moveToHead(node);
            return node.value;
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void put(K key, V value, int ttlInSeconds){
        lock.writeLock().lock();
        try{
            var node = map.get(key);
            if(node != null){
                runningValueSum -= node.value.doubleValue();
                runningValueSum += value.doubleValue();
                node.value = value;
                node.expiryTimestamp = calculateExpiry(ttlInSeconds);
                linkedList.moveToHead(node);
                return;
            }
            if(map.size() == capacity){
                evictExpired();
                if(map.size() == capacity){
                    node = linkedList.removeLast();
                    map.remove(node.key);
                    runningValueSum -= node.value.doubleValue();
                }
            }
            // Create New
            node = new Node<>(key, value, -1);
            node.expiryTimestamp = calculateExpiry(ttlInSeconds);
            linkedList.addToHead(node);
            map.put(key, node);
        }finally {
            lock.writeLock().unlock();
        }

    }

    public double getAverage(){
        lock.writeLock().lock();
        try {
            evictExpired();
        }finally {
            lock.writeLock().unlock();
        }

        lock.readLock().lock();
        try {
            return map.isEmpty() ? 0.0d : runningValueSum / map.size();
        }finally {
            lock.readLock().unlock();
        }
    }

    private long calculateExpiry(int ttlInSeconds) {
        return ttlInSeconds == -1 ? -1 : System.currentTimeMillis() + (ttlInSeconds * 1000L);
    }

    private void evictExpired() {
        long now = System.currentTimeMillis();
        for(var node = linkedList.tail.prev; node != linkedList.head; node = node.prev){
            if(node.isExpired(now)){
                var prev = node.prev;
                removeEntry(node);
                node = prev;
            }
        }
    }

    private void removeEntry(Node<K, V> node){
        if(node == null){
            return;
        }
        runningValueSum -= node.value.doubleValue();
        linkedList.remove(node);
        map.remove(node.key);
    }


    static class DoublyLinkedList<K, V extends Number>{
        private final Node<K, V> head;
        private final Node<K, V> tail;

        DoublyLinkedList(){
            this.head = new Node<>();
            this.tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }

        public void addToHead(Node<K, V> node){
            node.next = head.next;
            node.prev = head;
            head.next = node;
            node.next.prev = node;
        }

        public Node<K, V> removeLast(){
            var last = tail.prev;
            if(last == head){
                return null;
            }
            tail.prev = last.prev;
            last.prev.next = tail;
            return last;
        }

        public void remove(Node<K, V> node){
            var prev = node.prev;
            var next = node.next;
            prev.next = next;
            next.prev = prev;
        }

        public void moveToHead(Node<K, V> node){
            remove(node);
            addToHead(node);
        }
    }

    static class Node<K, V extends Number>{
        K key;
        V value;
        Node<K, V> next, prev;
        long expiryTimestamp;

        Node(){

        }
        Node(K key, V value, long expiryTimestamp){
            this.key = key;
            this.value = value;
            this.expiryTimestamp = expiryTimestamp;
        }
        public boolean isExpired(long now){
            return expiryTimestamp != -1 &&  expiryTimestamp <= now;
        }
    }
}
