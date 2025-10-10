package ankit.lld.mergeintervals.multiplestores.model;

public class Shift {
    private final int storeId;
    private final int employeeId;
    private final int start;
    private final int end;

    public Shift(int storeId,int employeeId, int start, int end){
        this.storeId = storeId;
        this.employeeId = employeeId;
        this.start = start;
        this.end = end;
    }

    public int getEmployeeId(){
        return this.employeeId;
    }
}
