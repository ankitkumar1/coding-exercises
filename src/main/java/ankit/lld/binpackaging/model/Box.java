package ankit.lld.binpackaging.model;

public class Box extends Cuboid{
    int boxId;
    public Box(int boxId, int length, int width, int height) {
        super(length, width, height);
        this.boxId = boxId;
    }

    @Override
    public String toString(){
        return "Box-"+boxId+":"+this.volume;
    }
}
