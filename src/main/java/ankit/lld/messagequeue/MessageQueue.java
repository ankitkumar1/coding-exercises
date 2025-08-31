package ankit.lld.messagequeue;

import ankit.lld.messagequeue.components.ISubscriber;
import ankit.lld.messagequeue.components.Message;
import ankit.lld.messagequeue.components.Topic;
import ankit.lld.messagequeue.components.TopicSubscriber;
import ankit.lld.messagequeue.handler.TopicHandler;

import java.util.HashMap;
import java.util.Map;



public class MessageQueue {

    private final Map<String, TopicHandler> topicMap;

    public MessageQueue(){
        this.topicMap = new HashMap<>();
    }

    public Topic addTopic(String name){
        Topic topic = new Topic(name);
        TopicHandler topicHandler = new TopicHandler(topic);
        topicMap.put(name, topicHandler);
        System.out.println("Created topic: " + topic.getName());
        return topic;
    }

    public void subscribe(ISubscriber subscriber, Topic topic){
        topic.addSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getName() + " subscribed to topic: " + topic.getName());
    }

    public void publishMessage(Message message, Topic topic){
        topic.addMessage(message);
        new Thread(() -> topicMap.get(topic.getName()).publish()).start();
        System.out.println(message.message() + " published to topic: " + topic.getName());
    }

}
