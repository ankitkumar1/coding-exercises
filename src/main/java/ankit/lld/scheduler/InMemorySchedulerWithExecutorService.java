package ankit.lld.scheduler;

import java.time.Instant;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Now I wanted to separate the schedule and execution logic..
 * So, the threads in the pool will be started lazely.
 * we can also scale both separately if needed.
 * for now keeping a single thread for scheduling and pool for execution.
 * */
public class InMemorySchedulerWithExecutorService {

    private final PriorityQueue<ScheduledTask> tasksQueue;
    private final ExecutorService workers;
    private final AtomicBoolean running;

    public InMemorySchedulerWithExecutorService(int threadPoolSize){
        tasksQueue = new PriorityQueue<>(Comparator.comparing(t -> t.nextExecutionTime));
        workers = Executors.newFixedThreadPool(threadPoolSize);
        this.running = new AtomicBoolean(true);
        new Thread(this::schedule).start();
    }

    // API 1: Schedule with time for single shot task.
    public void scheduleTask(Runnable runnable, Instant time){
        synchronized (tasksQueue){
            tasksQueue.offer(new OnShotTask(runnable, time));
            tasksQueue.notifyAll();
        }
    }

    // API 2: Schedule repeated task with fixed interval
    public void scheduleRepeatedTask(Runnable runnable, Instant firstExecutionTime, long intervalInSeconds){
        synchronized (tasksQueue){
            tasksQueue.offer(new FixedIntervalTask(runnable, firstExecutionTime, intervalInSeconds));
            tasksQueue.notifyAll();
        }
    }

    public void shutDown(){
        running.set(false);
        synchronized (tasksQueue) {
            tasksQueue.notifyAll();
        }
    }


    private void schedule(){
        while(running.get()){
            ScheduledTask task;
            synchronized (tasksQueue){
                // If the queue is empty and the scheduler is still running then wait.
                while(tasksQueue.isEmpty() && running.get()){
                    try{
                        tasksQueue.wait();
                    }catch(InterruptedException e){

                    }
                }
                if(!running.get()){
                    break;
                }
                task = tasksQueue.peek();
                long delay = task.nextExecutionTime.toEpochMilli() - Instant.now().toEpochMilli();
                if(delay > 0 ){
                    try{
                        tasksQueue.wait(delay);
                    }catch(InterruptedException e){

                    }
                    continue;
                }
                tasksQueue.poll();
            }
            workers.submit(() -> {
                execute(task);
            });
        }
    }

    private void execute(ScheduledTask task){
        task.runnable.run();
        if(task.canBeRescheduled()){
            task.reschedule();
            synchronized (tasksQueue){
                tasksQueue.offer(task);
                tasksQueue.notifyAll();
            }
        }
    }
}

