package ankit.lld.messagequeue.components;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber {

    private final AtomicInteger offset;
    private final ISubscriber subscriber;

    public TopicSubscriber(ISubscriber subscriber){
        this.offset = new AtomicInteger();
        this.subscriber = Objects.requireNonNull(subscriber);
    }

    public AtomicInteger getOffset(){
        return this.offset;
    }

    public ISubscriber getSubscriber(){
        return this.subscriber;
    }
}
