entities:
ParkingLot 
 - List<Floor>
 - ParkingEntry
 - ParkingExit
 - List<Display>
Floor
  - int floorNo;
  - List<Spots> spots
Spot
 - id
 - SpotType
 - SpotStatus

enum SpotType{
    CAR, TRUCK, BIKE
}

enum SpotStatus{
    AVAILABLE, OCCUPIED, MAINTENANCE
}

Vehicle{
    vehicleNo;
    type;
    ticket: ParkingTicket
    assignParking();
}

ParkingEntry
 - checkin()
ParkingExit
 - checkout()
ParkingTicket
    - id;
    - vehicleNo
    - entryTime
    - vehicleType
    - Spot
Payment

Display
 - Observer from ParkingLot for availablity.
