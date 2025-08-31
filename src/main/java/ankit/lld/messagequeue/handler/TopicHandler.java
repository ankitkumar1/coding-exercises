package ankit.lld.messagequeue.handler;

import ankit.lld.messagequeue.components.Topic;
import ankit.lld.messagequeue.components.TopicSubscriber;

import java.util.HashMap;
import java.util.Map;


public class TopicHandler {

    private Topic topic;
    private final Map<String, SubscriberWorker> subscriberWorkers;
    
    public TopicHandler(final Topic topic) {
        this.topic = topic;
        this.subscriberWorkers = new HashMap<>();
    }

    public void publish(){
        for(TopicSubscriber subscriber : topic.getSubscribers()){
            startSubsriberWorker(subscriber);
        }
    }

    private void startSubsriberWorker(TopicSubscriber subscriber){
        String subscriberId = subscriber.getSubscriber().getName();
        SubscriberWorker worker = subscriberWorkers.get(subscriberId);
        if(worker == null){
            worker = new SubscriberWorker(topic, subscriber);
            subscriberWorkers.put(subscriberId, worker);
            new Thread(worker).start();
        }else{
            worker.wakeUp();
        }
    }
}
