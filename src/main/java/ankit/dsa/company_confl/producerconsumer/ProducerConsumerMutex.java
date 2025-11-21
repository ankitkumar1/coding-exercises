package ankit.dsa.company_confl.producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerMutex {

    Queue<Integer> queue = new LinkedList<>();
    private final int MAX_SIZE = 5;

    public void produce() throws InterruptedException {
        int value = 0;
        while(true){
            synchronized (this){
                while(queue.size() == MAX_SIZE){
                    System.out.println("Producer waiting because size is full");
                    wait();
                }
                System.out.println("Produced : "+value);
                queue.offer(value++);
                notify();
            }
            Thread.sleep(500);
        }
    }

    public void consume() throws InterruptedException {
        while(true){
            synchronized (this){
                while(queue.isEmpty()){
                    wait();
                }

                System.out.println("Consumed : "+queue.poll());
                notify();
            }
            Thread.sleep(1000);
        }
    }


    public static void main(String[] args) {
        ProducerConsumerMutex producerConsumer = new ProducerConsumerMutex();

        Thread producer = new Thread(() -> {
            try {
                producerConsumer.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                producerConsumer.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        consumer.start();
        producer.start();
    }


}
