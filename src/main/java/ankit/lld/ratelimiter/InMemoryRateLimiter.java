package ankit.lld.ratelimiter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryRateLimiter {
    public static void main(String[] args) {
        UserService userService = new UserService();
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);
        Runnable runnable1 = () -> {
            System.out.println("User 1: "+userService.callService(100L));
        };
        //threadPool.scheduleAtFixedRate(runnable1, 0, 2, TimeUnit.SECONDS);

        threadPool.scheduleAtFixedRate(() -> {
            System.out.println("User 2: "+userService.callService(200L));
        }, 0, 5, TimeUnit.SECONDS);
    }
}

class UserService{
    RateLimiterService service;
    UserService(){
        this.service = RateLimiterService.getInstance();
    }
    public String callService(long userId){
        if(service.rateLimited(userId)){
            return "429 - Rate Limited!";
        }
        return "SUCCESS";
    }
}

class RateLimiterService {

    private static volatile RateLimiterService instance;
    private final Map<Long, RateLimiter> rateLimiterRegistry;

    private RateLimiterService(){
        rateLimiterRegistry = new ConcurrentHashMap<>();
        rateLimiterRegistry.put(100L, new SlidingWindow(new RateLimitRule(3, 10)));
        rateLimiterRegistry.put(200L, new SlidingWindow(new RateLimitRule(2, 20)));
    }

    public boolean rateLimited(long userId){
        return !rateLimiterRegistry.get(userId).allowed();
    }

    public static RateLimiterService getInstance(){
        if(instance == null){
            synchronized (RateLimiterService.class){
                if(instance ==  null){
                    instance = new RateLimiterService();
                }
            }
        }
        return instance;
    }
}

class RateLimitRule{
    int limit;
    int timeInSeconds;

    RateLimitRule(int limit, int timeInSeconds){
        this.limit = limit;
        this.timeInSeconds = timeInSeconds;
    }
}

interface RateLimiter{
    boolean allowed();
}

class SlidingWindow implements RateLimiter{
    private final Queue<Long> queue;
    private final RateLimitRule rule;

    public SlidingWindow(RateLimitRule rule){
        this.rule = rule;
        this.queue = new LinkedList<>();
    }

    @Override
    public boolean allowed() {
        Long currentTime = System.currentTimeMillis();
        checkAndUpdateWindow(currentTime);
        if(this.queue.size() < this.rule.limit){
            this.queue.offer(currentTime);
            return true;
        }
        return false;
    }

    private void checkAndUpdateWindow(Long currentTime) {
        if(queue.isEmpty()){
            return;
        }
        long time = currentTime - queue.peek();
        while(time >= rule.timeInSeconds * 1000L){
            queue.poll();
            if(queue.isEmpty()){
                break;
            }
            time = currentTime - queue.peek();
        }
    }
}

class LeakyBucket implements RateLimiter{
    private int capacity;
    private int leakRate;

    @Override
    public boolean allowed() {
        return false;
    }
}
