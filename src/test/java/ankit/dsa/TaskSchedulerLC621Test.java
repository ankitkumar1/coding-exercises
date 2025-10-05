package ankit.dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskSchedulerLC621Test {

    @Test
    public void test(){
        TaskSchedulerLC621 schedulerLC621 = new TaskSchedulerLC621();
        assertEquals(8, schedulerLC621.leastInterval(new char[]{'A','A','A','B','B','B'}, 2));
    }

}
