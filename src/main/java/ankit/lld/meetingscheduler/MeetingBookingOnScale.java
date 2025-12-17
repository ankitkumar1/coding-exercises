package ankit.lld.meetingscheduler;

import java.util.TreeMap;

/**
 * write a method book(int startTime, int endTime),
 * if there is any overlapping booking for this interval then return false,
 * else book it and save the interval in memory.
 * */
public class MeetingBookingOnScale {

    private final TreeMap<Integer, Integer> bookings = new TreeMap<>();

    public boolean book(int start, int end){
        if(bookings.isEmpty()){
            bookings.put(start, end);
        }

        Integer prevStart = bookings.floorKey(start);
        // check if start overlapping this interval
        if(prevStart != null &&  bookings.get(prevStart) > start){
            return false;
        }

        // check for ceiling
        Integer nextStart = bookings.ceilingKey(end);
        if(nextStart != null && nextStart > end){
            return false;
        }

        bookings.put(start, end);
        return true;
    }

    public static void main(String[] args) {
        MeetingBookingOnScale meetingBooking  = new MeetingBookingOnScale();
        System.out.println(meetingBooking.book(10, 15));
        System.out.println(meetingBooking.book(15, 16));
        System.out.println(meetingBooking.book(9, 11));
    }
}
