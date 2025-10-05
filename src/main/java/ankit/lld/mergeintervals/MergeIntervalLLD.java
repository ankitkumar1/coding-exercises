package ankit.lld.mergeintervals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeIntervalLLD {

    public static void main(String[] args) {
        MergeIntervalService service = new MergeIntervalService(new DefaultOverlapPolicy());
        //[1,3], [2,6], [8,10], [15,18]
        System.out.println(service.mergeInterval(new ArrayList<>(List.of(new Interval(1, 3),
                new Interval(2, 6),new Interval(8, 10),new Interval(15, 18)))));
        System.out.println(service.mergeInterval(new ArrayList<>(List.of(new Interval(8, 10),new Interval(15, 18),
                new Interval(1, 3), new Interval(3, 6), new Interval(17, 20)))));
    }
}

class MergeIntervalService{
    private final OverlapPolicy overlapPolicy;

    public MergeIntervalService(OverlapPolicy overlapPolicy){
        this.overlapPolicy = overlapPolicy;
    }

    public List<Interval> mergeInterval(List<Interval> intervals){
        List<Interval> mergedIntervals = new ArrayList<>();
        intervals.sort(Comparator.comparingInt(i -> i.start));
        int start = intervals.getFirst().start;
        int end = intervals.getFirst().end;
        for(int i=1; i<intervals.size(); i++){
            Interval current = intervals.get(i);
            if(overlapPolicy.overlap(new Interval(start, end), current)){
                end = Math.max(end, current.end);
            }else{
                mergedIntervals.add(new Interval(start, end));
                start = current.start;
                end = current.end;
            }
        }
        mergedIntervals.add(new Interval(start, end));
        return mergedIntervals;
    }
}

class Interval{
    int start;
    int end;
    public Interval(int start, int end){
        this.start = start;
        this.end = end;
    }
    @Override
    public String toString() {
        return start+":"+end;
    }
}

interface OverlapPolicy{
    boolean overlap(Interval a, Interval b);
}

class DefaultOverlapPolicy implements OverlapPolicy{

    @Override
    public boolean overlap(Interval a, Interval b) {
        return b.start < a.end;
    }
}