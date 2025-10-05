package ankit.lld.binpackaging;

import ankit.lld.binpackaging.model.Box;
import ankit.lld.binpackaging.model.Truck;

import java.util.List;
import java.util.Map;

public class BinPackagingResponse{
    private final String status;
    private final Map<Truck, List<Box>> placedMap;

    BinPackagingResponse(Builder builder){
        this.status = builder.status;
        this.placedMap = builder.placedMap;
    }

    public String getStatus(){
        return this.status;
    }

    public Map<Truck, List<Box>> getPlacedMap(){
        return this.placedMap;
    }


    public static class Builder{
        private String status;
        private Map<Truck, List<Box>> placedMap;

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder placedMap(Map<Truck, List<Box>> placedMap) {
            this.placedMap = placedMap;
            return this;
        }

        public BinPackagingResponse build() {
            return new BinPackagingResponse(this);
        }
    }
}
