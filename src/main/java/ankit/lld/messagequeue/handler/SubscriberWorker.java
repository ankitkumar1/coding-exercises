package ankit.lld.messagequeue.handler;


import ankit.lld.messagequeue.components.Message;
import ankit.lld.messagequeue.components.Topic;
import ankit.lld.messagequeue.components.TopicSubscriber;

public class SubscriberWorker implements Runnable{

    private final Topic topic;
    private final TopicSubscriber subscriber;

    public SubscriberWorker(Topic topic, TopicSubscriber subscriber){
        this.topic = topic;
        this.subscriber = subscriber;
    }

    @Override
    public void run() {
        synchronized(subscriber){
            while(true){
                int offset = subscriber.getOffset().get();
                while(offset >= topic.getMessages().size()){
                    try{
                        subscriber.wait();
                    }catch(InterruptedException e){
                        System.out.println("Thread interrupted");
                    }
                }
                Message message = topic.getMessages().get(offset);
                try{
                    subscriber.getSubscriber().consume(message);
                    // We cannot just increment here since subscriber offset can be reset while it is consuming. So, after
                    // consuming we need to increase only if it was previous one.
                    subscriber.getOffset().compareAndSet(offset, offset+1);
                }catch(InterruptedException e){
                    System.out.println("Thread interrupted");
                }
            }
        }
    }

    public void wakeUp() {
        synchronized (subscriber) {
            subscriber.notify();
        }
    }
}
