package ankit.lld.scheduler;

import java.time.Instant;

public class Test {

    public static void main(String[] args) {
        //testScheduler();

        testSchedulerWithExecutorService();
    }

    public static void testScheduler(){
        InMemoryScheduler scheduler = new InMemoryScheduler(5);

        scheduler.scheduleTask(() -> System.out.println("Task 1 executed ! "), Instant.now().plusSeconds(5));
        scheduler.scheduleTask(() -> System.out.println("Task 2 executed ! "), Instant.now().plusSeconds(1));

        scheduler.scheduleRepeatedTask(() -> System.out.println("Task 3 Repeatable task executed! "), Instant.now(), 3);

        try {
            Thread.sleep(15000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scheduler.shutDown();
    }

    public static void testSchedulerWithExecutorService(){
        InMemorySchedulerWithExecutorService scheduler = new InMemorySchedulerWithExecutorService(5);

        scheduler.scheduleTask(() -> System.out.println("Task 1 executed ! "), Instant.now().plusSeconds(5));
        scheduler.scheduleTask(() -> System.out.println("Task 2 executed ! "), Instant.now().plusSeconds(1));

        scheduler.scheduleRepeatedTask(() -> System.out.println("Task 3 Repeatable task executed! "), Instant.now(), 3);

        try {
            Thread.sleep(15000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scheduler.shutDown();
    }
}
