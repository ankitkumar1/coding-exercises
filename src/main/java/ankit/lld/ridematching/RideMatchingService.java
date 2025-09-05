package ankit.lld.ridematching;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RideMatchingService {

}

class DriverLocationService {
    private ConcurrentMap<Long, Driver> availableDrivers = new ConcurrentHashMap<>();
    private final AtomicLong driverIdGenerator = new AtomicLong(0);

    public Driver registerDriver(String name, VehicleType vehicleType, Location currentLocation){
        Driver driver = new Driver(driverIdGenerator.incrementAndGet(), name, vehicleType, currentLocation);
        availableDrivers.put(driver.id, driver);
        return driver;
    }

    public Driver removeDriver(long driverId){
        return availableDrivers.remove(driverId);
    }

    public void updateDriverLocation(long driverId, Location location){
        availableDrivers.get(driverId).currentLocation = location;
    }

    public List<Driver> findNearestDrivers(Location location, double maxDistance){
        return availableDrivers.values().stream()
                    .filter((driver) -> driver.currentLocation.getDistance(location) <=maxDistance)
                .collect(Collectors.toList());
    }
}

class RideBookingService{

}

class Rider{
    long id;
    String riderName;
}

class RideRequest{
    Rider rider;
    Location location;

    public RideRequest(Rider rider, Location location){
        this.rider = rider;
        this.location = location;
    }
}

class Location{
    double latitude;
    double longitude;

    public Location(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    double getDistance(Location location){
        double latitudeDiff = location.latitude-latitude;
        double longitudeDiff = location.longitude-longitude;
        return Math.sqrt((latitudeDiff* latitudeDiff) + (longitudeDiff*longitudeDiff));
    }

    @Override
    public String toString(){
        return latitude+"_"+longitude;
    }
}

class Driver{
    long id;
    String name;
    Location currentLocation;
    VehicleType vehicleType;
    AtomicBoolean isAvailable = new AtomicBoolean(true);

    public Driver(long id, String name, VehicleType vehicleType, Location currentLocation){
        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.currentLocation = currentLocation;
    }
}

enum VehicleType {
    HATCHBACK,
    SEDAN,
    SUV
}

class VehicleFareEstimate{
    VehicleType vehicleType;
    double price;

    public  VehicleFareEstimate(VehicleType vehicleType,  double price){
        this.vehicleType = vehicleType;
        this.price = price;
    }
}
