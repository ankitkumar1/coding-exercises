package ankit.lld.irctc;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class IRCTCBookingSystemConcurrent {

    public static void main(String[] args){
        IRCTCBookingSystemConcurrent irctc = new IRCTCBookingSystemConcurrent();
        irctc.initiateApplication();
        new Thread(() -> {
            Optional<Booking> booking = irctc.train.bookSeats(1, 4, 5);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking.get());
        }, "Thread1").start();

        new Thread(() -> {
            Optional<Booking> booking = irctc.train.bookSeats(1, 4, 5);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking.get());
        }, "Thread5").start();

        new Thread(() -> {
            Optional<Booking> booking = irctc.train.bookSeats(1, 4, 5);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking.get());
        }, "Thread6").start();

        new Thread(() -> {
            Optional<Booking> booking2 = irctc.train.bookSeats(1, 4, 5);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking2.get());
        }, "Thread2").start();

        new Thread(() -> {
            Optional<Booking> booking3 = irctc.train.bookSeats(4, 5, 6);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking3.get());
        }, "Thread3").start();

        new Thread(() -> {
            Optional<Booking> booking3 = irctc.train.bookSeats(4, 5, 6);
            System.out.println("["+Thread.currentThread().getName()+"]"+ booking3.get());
        }, "Thread4").start();

    }

    Train train = null;
    private void initiateApplication(){
        var stations = List.of(new Station("A", 1),
                new Station("B", 2), new Station("C", 3),
        new Station("D", 4), new Station("E", 5));

        var seats = new ArrayList<Seat>();
        for(int i = 1; i<=5; i++){
            seats.add(new Seat(1, i));
        }
        for(int i = 1; i<=5; i++){
            seats.add(new Seat(2, i));
        }

        TrainBuilder trainBuilder = new TrainBuilder(100);
        train = trainBuilder.addStations(stations).addSeats(seats).build();
    }

}

class Station{
    String name;
    int sequence;

    public Station(String name, int sequence){
        this.name = name;
        this.sequence = sequence;
    }
}

class Train{
    int trainNo;
    List<Station> stations;
    List<Seat> seats;
    public Train(int trainNo, List<Station> stations, List<Seat> seats){
        this.trainNo = trainNo;
        this.stations = Collections.unmodifiableList(stations);
        this.seats = Collections.unmodifiableList(seats);
    }

    Optional<Booking> bookSeats(int from, int to, int noOfSeats){
        // Check the availablity and get lock on the available seat.
        var acquiredLocks = new ArrayList<ReentrantLock>();
        List<Seat> availableSeats = new ArrayList<>(noOfSeats);
        for(Seat seat : this.seats){
            if(seat.isAvailable(from, to)){
                availableSeats.add(seat);
                seat.lock.lock();
                acquiredLocks.add(seat.lock);
            }
            if(availableSeats.size() == noOfSeats){
                break;
            }
        }
        if(availableSeats.size() < noOfSeats){
            acquiredLocks.forEach(ReentrantLock::unlock);
            throw new RuntimeException("Required No of seats not available!");
        }

        // Step 2: Now we got the lock on the required seats. lets book those seat.
        Booking booking = new Booking(availableSeats, from, to);
        try{
            availableSeats.forEach(seat -> seat.addBooking(booking));
        }finally {
            acquiredLocks.forEach(ReentrantLock::unlock);
        }
        return Optional.of(booking);
    }
}

class TrainBuilder{
    int trainNo;
    List<Station> stations;
    List<Seat> seats;

    public TrainBuilder(int trainNo){
        this.trainNo = trainNo;
    }

    public TrainBuilder addStations(List<Station> stations){
        this.stations = stations;
        return this;
    }

    public TrainBuilder addSeats(List<Seat> seats){
        this.seats = seats;
        return this;
    }

    public Train build(){
        return new Train(trainNo, stations, seats);
    }

}

class Seat{
    int seatNo;
    int coachNo;
    List<Booking> bookings;
    ReentrantLock lock = new ReentrantLock();

    public Seat(int coachNo, int seatNo){
        this.coachNo = coachNo;
        this.seatNo = seatNo;
        this.bookings = new ArrayList<>();
    }

    public boolean isAvailable(int from, int to){
        return bookings.stream().allMatch(booking ->
                from >= booking.to || to <= booking.from);
    }

    void addBooking(Booking booking){
        this.bookings.add(booking);
    }

    @Override
    public String toString(){
        return "Seat No : "+seatNo+", Coach No: "+coachNo;
    }
}

class Booking{
    UUID id;
    List<Seat> seats;
    int from;
    int to;

    public Booking(List<Seat> seats, int from, int to){
        this.id = UUID.randomUUID();
        this.seats = seats;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Booking{" + id + ", seats=" + seats + ", from=" + from + ", to=" + to;
    }

}
