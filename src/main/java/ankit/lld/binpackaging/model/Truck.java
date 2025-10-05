package ankit.lld.binpackaging.model;

public class Truck extends Cuboid{
    long occupiedVolume = 0L;
    int truckId;

    public Truck(int truckId, int length, int width, int height) {
        super(length, width, height);
        this.truckId = truckId;
    }

    public boolean place(Box box){
        if(box.getVolume() > (this.volume - occupiedVolume)){
            return false;
        }
        occupiedVolume += box.getVolume();
        return true;
    }

    public boolean canPlace(Box box){
        return box.getVolume() <= (this.volume - occupiedVolume);
    }

    public long getAvailableVolume(){
        return this.volume - occupiedVolume;
    }

    @Override
    public String toString(){
        return "Truck-"+truckId+":"+this.volume;
    }
}
