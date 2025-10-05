package ankit.lld.binpackaging.model;

public abstract class Cuboid{
    int length;
    int width;
    int height;
    long volume;

    public Cuboid(int length, int width, int height){
        this.length = length;
        this.width = width;
        this.height = height;
        this.volume = (long)this.length * this.height * this.width;
    }

    public long getVolume(){
        return this.volume;
    }
}
