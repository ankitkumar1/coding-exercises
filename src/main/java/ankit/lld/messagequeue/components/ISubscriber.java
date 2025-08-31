package ankit.lld.messagequeue.components;

public interface ISubscriber {

    String getName();
    void consume(Message message) throws InterruptedException;

}
