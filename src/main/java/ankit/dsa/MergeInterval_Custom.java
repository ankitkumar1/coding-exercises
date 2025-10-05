package ankit.dsa;

import java.util.*;

public class MergeInterval_Custom {

    public List<Interval> merge(List<Interval> intervalList) {
        intervalList.sort(Comparator.comparing(Interval::start)
                .thenComparing(Interval::start));
        OverlapPolicy overlapPolicy = new DefaultPolicy();
        var result = new ArrayList<Interval>();
        Interval prev = intervalList.getFirst();

        for(int i=1; i<intervalList.size(); i++){
            Interval current = intervalList.get(i);
            if(overlapPolicy.overlap(prev, current)){
                int end = Math.max(prev.end(), current.end());
                prev = new Interval(prev.start(), end);
            }else{
                result.add(prev);
                prev = current;
            }
        }
        result.add(prev);
        return result;
    }
}

record Interval(int start, int end){}

interface OverlapPolicy{
    boolean overlap(Interval prev, Interval next);
}

class DefaultPolicy implements OverlapPolicy{

    @Override
    public boolean overlap(Interval prev, Interval next){
        return next.start() <= prev.end();
    }
}
