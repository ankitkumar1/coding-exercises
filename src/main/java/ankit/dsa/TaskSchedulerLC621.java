package ankit.dsa;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class TaskSchedulerLC621 {

    public int leastInterval(char[] tasks, int n) {
        var uniqueTaskMap = new HashMap<Character, Task>();
        for(char taskId : tasks){
            uniqueTaskMap.putIfAbsent(taskId, new Task(taskId));
            uniqueTaskMap.get(taskId).remainingTask = uniqueTaskMap.get(taskId).remainingTask + 1;
        }

        var heap = new PriorityQueue<Task>(Comparator.comparingInt(t -> t.nextExecutionTime));
        heap.addAll(uniqueTaskMap.values());

        int currentTime = 0;
        while(!heap.isEmpty()){
            if(heap.peek().nextExecutionTime <= currentTime){
                Task current = heap.poll();
                current.remainingTask = current.remainingTask - 1;
                if(current.remainingTask > 0){
                    current.nextExecutionTime = current.nextExecutionTime + n + 1;
                    heap.add(current);
                }
            }
            currentTime++;
        }
        return currentTime;
    }

    static class Task{
        char taskId;
        int remainingTask = 0;
        int nextExecutionTime = 0;
        public Task(char taskId){
            this.taskId = taskId;
        }
    }

    public static void main(String[] args) {
        TaskSchedulerLC621 obj = new TaskSchedulerLC621();

        System.out.println(obj.leastInterval(new char[]{'A','A','A','B','B','B'}, 2)); // 8
        System.out.println(obj.leastInterval(new char[]{'A','C','A','B','D','B'}, 1)); // 6
        System.out.println(obj.leastInterval(new char[]{'A','A','A','B','B','B'}, 3)); // 10
        System.out.println(obj.leastInterval(new char[]{'B','C','D','A','A','A','A','G'}, 1)); // 8
    }
}
