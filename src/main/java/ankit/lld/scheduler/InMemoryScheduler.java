package ankit.lld.scheduler;

import java.time.Instant;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class InMemoryScheduler {

    private final PriorityQueue<ScheduledTask> tasksQueue;
    private final Thread[] workers;
    private final AtomicBoolean running = new AtomicBoolean(true);;

    public InMemoryScheduler(int threadPoolSize){
        tasksQueue = new PriorityQueue<>(Comparator.comparing(t -> t.nextExecutionTime));
        workers = new Thread[threadPoolSize];
        for(int i=0; i<threadPoolSize; i++){
            workers[i] = new Thread(this::execute, "Worker - "+i);
            workers[i].start();
        }
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


    private void execute(){
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
            // Come out of syncronized to execute the task independently.
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

}

abstract class ScheduledTask{
    Runnable runnable;
    Instant nextExecutionTime;

    public ScheduledTask(Runnable runnable, Instant nextExecutionTime){
        this.runnable = runnable;
        this.nextExecutionTime = nextExecutionTime;
    }

    abstract boolean canBeRescheduled();
    abstract void reschedule();
}

class OnShotTask extends  ScheduledTask{

    public OnShotTask(Runnable runnable, Instant nextExecutionTime) {
        super(runnable, nextExecutionTime);
    }

    @Override
    boolean canBeRescheduled() {
        return false;
    }

    @Override
    void reschedule() {
        System.out.println("This is single shot task. Can't be rescheduled!");
    }
}

class FixedIntervalTask extends ScheduledTask{

    private final long intervalInSeconds;

    public FixedIntervalTask(Runnable runnable, Instant nextExecutionTime, long intervalInSeconds) {
        super(runnable, nextExecutionTime);
        this.intervalInSeconds = intervalInSeconds;
    }

    @Override
    boolean canBeRescheduled() {
        return true;
    }

    @Override
    void reschedule() {
        this.nextExecutionTime = Instant.now().plusSeconds(this.intervalInSeconds);
    }
}
