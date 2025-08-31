package ankit.lld.messagequeue.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Topic {
    String name;
    private List<Message> messages;
    private List<TopicSubscriber> subscribers;

    public Topic(String name){
        this.name = Objects.requireNonNull(name);
        this.messages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public synchronized void addMessage(Message message){
        messages.add(message);
    }

    public synchronized void addSubscriber(TopicSubscriber subscriber){
        subscribers.add(subscriber);
    }

    public List<Message> getMessages(){
        return this.messages;
    }

    public List<TopicSubscriber> getSubscribers(){
        return this.subscribers;
    }

    public String getName(){
       return this.name;
    }
}
