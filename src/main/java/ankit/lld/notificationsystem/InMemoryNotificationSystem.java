package ankit.lld.notificationsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryNotificationSystem {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();
        service.sendNotification("Ankit", "Anushka", "Hello",  ChannelType.EMAIL);
        service.sendNotification("Ankit", "Anushka", "Hello",  ChannelType.SMS);
        service.sendNotification("U1", "U2", "Welcome",  ChannelType.SMS);
        service.sendNotification("U2", "U3", "Hi",  ChannelType.SMS);
        service.sendNotification("U3", "U4", "Message",  ChannelType.SMS);
    }
}

enum NotificationStatus{
    PENDING, SENT, FAILED
}

enum ChannelType{
    EMAIL, SMS
}

class NotificationService{
    private BlockingDeque<NotificationRequest> requests;
    private Map<ChannelType, NotificationChannel> channels;
    private NotificationDispatcher dispatcher;
    private Map<Integer, NotificationStatus> statusMap;

    public NotificationService(){
        this.requests = new LinkedBlockingDeque<>();
        this.statusMap = new HashMap<>();
        this.channels = new HashMap<>();
        this.channels.put(ChannelType.EMAIL, new EmailNotificationChannel());
        this.channels.put(ChannelType.SMS, new SMSNotificationChannel());
        this.dispatcher = new NotificationDispatcher(this.requests, this.channels, this.statusMap);
        this.dispatcher.start();
    }

    public void sendNotification(String sender, String recipient, String message,
                                    ChannelType channelType){
        NotificationRequest request = new NotificationRequest(sender, recipient, message, channelType);
        statusMap.put(request.id, NotificationStatus.PENDING);
        this.requests.add(request);
    }

    public NotificationStatus getStatus(int id){
        return this.statusMap.get(id);
    }
}

class NotificationDispatcher{
    private final BlockingDeque<NotificationRequest> requests;
    private final Map<ChannelType, NotificationChannel> channels;
    private final Map<Integer, NotificationStatus> statusMap;
    private final ExecutorService executorService;

    public NotificationDispatcher(BlockingDeque<NotificationRequest> requests,
                                  Map<ChannelType, NotificationChannel> channels,
                                  Map<Integer, NotificationStatus> statusMap){
        this.requests = requests;
        this.channels = channels;
        this.statusMap = statusMap;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    public void start(){
        Runnable runnable = () -> {
          while(true){
              try {
                  NotificationRequest notificationRequest = this.requests.take();
                  send(notificationRequest);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
          }
        };

        for(int i=0; i<=5; i++){
            this.executorService.submit(runnable);
        }
    }

    private void send(NotificationRequest request) {
        NotificationChannel channel = this.channels.get(request.channelType);
        boolean success = channel.sendNotition(request);
        if(success){
            statusMap.put(request.id, NotificationStatus.SENT);
        }else{
            statusMap.put(request.id, NotificationStatus.FAILED);
        }
        // Here based on status code we can code retry mechanism.
    }


}

class NotificationRequest{
    int id;
    String sender;
    String recipient;
    String message;
    ChannelType channelType;

    public  NotificationRequest(String sender, String recipient, String message, ChannelType channelType){
        this.id = Utility.idGenerator.incrementAndGet();
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.channelType = channelType;
    }
}

interface NotificationChannel{
    boolean sendNotition(NotificationRequest request);
}

class EmailNotificationChannel implements NotificationChannel{

    @Override
    public boolean sendNotition(NotificationRequest request) {
        System.out.println(Thread.currentThread().getName()+ " - Notificaiton Sent : Channel Type - "+request.channelType+
                ", Sender - "+request.sender+ ", Reciever - "+request.recipient);
        return true;
    }
}

class SMSNotificationChannel implements NotificationChannel{

    @Override
    public boolean sendNotition(NotificationRequest request) {
        System.out.println(Thread.currentThread().getName()+ " - Notificaiton Sent : Channel Type - "+request.channelType+
                ", Sender - "+request.sender+ ", Reciever - "+request.recipient);
        return true;
    }
}

class Utility{
    static final AtomicInteger idGenerator = new AtomicInteger();
}
