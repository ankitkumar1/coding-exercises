package ankit.lld.messagequeue;


import ankit.lld.messagequeue.components.ISubscriber;
import ankit.lld.messagequeue.components.Message;

public class Subscriber implements ISubscriber {

    private String name;
    public Subscriber(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
    public void consume(Message message) throws InterruptedException {
        System.out.println("Message Consumed : "+ message);
    }

    

}
