package ankit.lld.mergeintervals.multiplestores;

import ankit.lld.mergeintervals.multiplestores.model.Shift;

import java.util.List;
import java.util.Map;

public interface ShiftMerger {
    Map<Integer, List<Shift>> mergeShift(List<Shift> shifts);
}
