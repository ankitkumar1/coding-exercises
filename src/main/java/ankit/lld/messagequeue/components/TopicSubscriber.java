package ankit.lld.messagequeue.components;

import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber {
    private AtomicInteger offset;
    private ISubscriber subscriber;

    public TopicSubscriber(ISubscriber subscriber){
        this.offset = new AtomicInteger(0);
        this.subscriber = subscriber;
    }

    public AtomicInteger getOffset(){
        return this.offset;
    }

    public ISubscriber getSubscriber(){
        return this.subscriber;
    }
}
