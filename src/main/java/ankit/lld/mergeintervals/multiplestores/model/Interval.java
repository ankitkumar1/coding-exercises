package ankit.lld.mergeintervals.multiplestores.model;

public class Interval {
    private final int start;
    private final int end;

    public Interval(int start, int end){
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
