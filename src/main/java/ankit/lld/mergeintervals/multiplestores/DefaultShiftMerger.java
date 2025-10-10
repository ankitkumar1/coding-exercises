package ankit.lld.mergeintervals.multiplestores;

import ankit.lld.mergeintervals.multiplestores.model.Shift;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultShiftMerger implements ShiftMerger{
    @Override
    public Map<Integer, List<Shift>> mergeShift(List<Shift> shifts) {

        System.out.println("Default");

        Map<Integer, List<Shift>> shiftMap =
                    shifts.stream().collect(Collectors.groupingBy(Shift::getEmployeeId));
        for(Integer employeeId : shiftMap.keySet()){
            List<Shift> mergedShift = mergeIntervals(shiftMap.get(employeeId));
            shiftMap.put(employeeId, mergedShift);
        }

        return shiftMap;
    }

    private List<Shift> mergeIntervals(List<Shift> value) {
        return value;
    }

    public static void main(String[] args) {
        DefaultShiftMerger obj = new DefaultShiftMerger();
        obj.mergeShift(List.of(new Shift(1,1,1,2)));
    }
}
