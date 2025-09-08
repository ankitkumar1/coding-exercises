package atlasian;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

public class Test2 {
    record Interval(LocalDateTime start, LocalDateTime end){
        @Override
        public String toString(){
            return start.toLocalTime()+" to "+end.toLocalTime();
        }
    };
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2025, 12, 31, 12, 15);
        LocalDateTime end = LocalDateTime.of(2025, 12, 31, 13, 15);
        System.out.println(start);
        System.out.println(start.isBefore(end));
        System.out.println(start.isAfter(end));

        TreeSet<Interval> intervals = new TreeSet<>(Comparator.comparing(Interval::start)
                    .thenComparing(Interval::end));

        intervals.add(new Interval(start, end));
        System.out.println(intervals);

        LocalDateTime start1 = LocalDateTime.of(2025, 12, 31, 10, 15);
        LocalDateTime end1 = LocalDateTime.of(2025, 12, 31, 10, 45);

        intervals.add(new Interval(start1, end1));
        System.out.println(intervals);

        LocalDateTime start2 = LocalDateTime.of(2025, 12, 31, 11, 15);
        LocalDateTime end2 = LocalDateTime.of(2025, 12, 31, 12, 15);

        intervals.add(new Interval(start2, end2));

        System.out.println(intervals);

        LocalDateTime start3 = LocalDateTime.of(2025, 12, 31, 13, 15);
        LocalDateTime end3 = LocalDateTime.of(2025, 12, 31, 14, 30);

        Interval interval = new Interval(start3, end3);
        boolean isOverlapping = intervals.stream().anyMatch(meetingInterval -> (interval.start().isAfter(meetingInterval.start()) &&
                interval.start().isBefore(meetingInterval.end())) ||
                interval.end().isAfter(meetingInterval.start()) &&
                        interval.end().isBefore(meetingInterval.end()));

        System.out.println(isOverlapping);
    }


}
