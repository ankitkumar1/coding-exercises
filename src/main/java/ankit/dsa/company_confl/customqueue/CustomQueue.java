package ankit.dsa.company_confl.customqueue;

/*
* Brute force solution: use an Arraylist
* Queue : add element at last. O(1)
* Dequeue : remove from first. O(n)
* Find in middle : get element at size()/2 index : O(1).
*
* Optimized approach:
* Use singly linked with middle pointer.
* enqueue : Add element at last : O(1)
* Dequeue: remove element from start : O(1)
* Find in middle: keep a middle pointer and update that pointer when there is enqueue/dequeue : O(1)
*
* if size is odd after enquing: middle++.
* If size is odd after dequeing: middle++.
*
* If asked for removeFromMiddle() kind of API. we can use DoublyLinkedList. it will give same time complexity.
*
* */

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomQueue<T> {
    Node<T> sentinel;
    Node<T> tail;
    Node<T> middle;
    int size = 0;
    private final Lock lock = new ReentrantLock();

    public CustomQueue(){
        sentinel = new Node<>(null);
        tail = sentinel;
        middle = sentinel;
    }

    public void enqueue(T value){
        var newNode = new Node<>(value);
        lock.lock();
        try{
            tail.next = newNode;
            tail = tail.next;
            ++size;
            if(size % 2 == 1){
                middle = middle.next;
            }
        }finally {
            lock.unlock();
        }
    }

    public Optional<T> deque(){
        lock.lock();
        try{
            var dequeedNode = sentinel.next;
            if(dequeedNode == null){
                return Optional.empty();
            }
            // Remove the dequed node from list.
            sentinel.next = dequeedNode.next;
            --size;
            if(size % 2 == 1){
                middle = middle.next;
            }
            return Optional.of(dequeedNode.value);
        }finally {
            lock.unlock();
        }
    }

    // Keeping this lock free since no modification. we can discuss if needed I can use read lock here.
    public Optional<T> findMiddle(){
        return sentinel == middle ? Optional.empty() : Optional.of(middle.value);
    }

    static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value){
            this.value = value;
        }
    }


    public static void main(String[] args) {
        var customQueue = new CustomQueue<Integer>();
        System.out.println("Middle should be empty : "+customQueue.findMiddle()); // Empty.
        System.out.println("Dequed should be Empty : "+customQueue.deque());
        customQueue.enqueue(1);
        System.out.println("Middle should be 1 : "+customQueue.findMiddle()); // 1
        customQueue.enqueue(2);
        System.out.println("Middle should be 1 : "+customQueue.findMiddle()); // 1
        customQueue.enqueue(3);
        System.out.println("Middle should be 2 : "+customQueue.findMiddle()); // 2
        customQueue.enqueue(4);
        System.out.println("Middle should be 2 : "+customQueue.findMiddle()); // 2
        customQueue.enqueue(5);
        System.out.println("Middle should be 3 : "+customQueue.findMiddle()); // 3
        customQueue.enqueue(6);
        System.out.println("Middle should be 3 : "+customQueue.findMiddle()); // 3

        System.out.println("Dequed should be 1 : "+customQueue.deque());
        System.out.println("Middle should be 4 : "+customQueue.findMiddle()); // 4

        System.out.println("Dequed should be 2 : "+customQueue.deque());
        System.out.println("Middle should be 4 : "+customQueue.findMiddle()); // 4

        System.out.println("Dequed should be 3 : "+customQueue.deque());
        System.out.println("Middle should be 5 : "+customQueue.findMiddle()); // 5
    }
}
