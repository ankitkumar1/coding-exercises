package ankit.lld.messagequeue;

import ankit.lld.messagequeue.components.Message;
import ankit.lld.messagequeue.components.Topic;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        MessageQueue queue = new MessageQueue();
        Topic topic1 = queue.addTopic("topic1");
        Topic topic2 = queue.addTopic("topic2");

        queue.subscribe(new Subscriber("subscriber-1_1"), topic1);
        queue.subscribe(new Subscriber("subscriber-1_2"), topic1);
        queue.subscribe(new Subscriber("subscriber-1_3"), topic1);

        queue.subscribe(new Subscriber("subscriber-2_1"), topic2);

        queue.publishMessage(new Message("Hello!"), topic1);

        queue.publishMessage(new Message("Welcome!"), topic2);

        Thread.sleep(10000);
        queue.publishMessage(new Message("Hello 2!"), topic1);
    }

}
