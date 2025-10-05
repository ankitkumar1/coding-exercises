package ankit.dsa;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;

public class SlidingWindowMax_LC239 {
    public static void main(String[] args) {
        printArray(maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3));
        printArray(maxSlidingWindow(new int[]{8,7,6,5,4,3,2,1}, 3));
        printArray(maxSlidingWindow(new int[]{1,2,3,4,5,6,7,8}, 3));
        printArray(maxSlidingWindow(new int[]{1,3,1,2,0,5}, 3));
    }

    public static int[] maxSlidingWindow(int[] nums, int k){
        Deque<Integer> deQ = new ArrayDeque<>();
        int[] result = new int[nums.length - k + 1];
        int j = 0;
        for(int i = 0; i<nums.length; i++){
            // Remove smaller values from right side to keep the queue monotonically decreasing.
            while(!deQ.isEmpty() && nums[i] > nums[deQ.peekLast()]){
                deQ.pollLast();
            }
            // Remove all out of bound values from left side.
            while(!deQ.isEmpty() && deQ.peekFirst() <= (i -k)){
                deQ.pollFirst();
            }
            deQ.offer(i);
            if(i >= k-1){
                result[j++] = nums[deQ.peekFirst()];
            }
        }
        return result;
    }

    public static void printArray(int[] array){
        System.out.print("[");
        for(int value : array){
            System.out.print(value + ",");
        }
        System.out.print("]");
        System.out.println();
    }
}
