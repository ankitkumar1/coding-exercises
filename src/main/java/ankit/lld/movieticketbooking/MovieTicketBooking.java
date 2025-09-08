package ankit.lld.movieticketbooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MovieTicketBooking {

    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService(paymentService);

        // Bootstrap user and theatre
        User user1 = new User("Ankit");

    }
}

class SearchService{

}

class BookingService{

    private final Map<Integer, List<Booking>> userBookingMap;
    PaymentService paymentService;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    BookingService(PaymentService paymentService){
        this.paymentService = paymentService;
        this.userBookingMap = new ConcurrentHashMap<>();
    }

    public BookingStatus bookMovieShow(User user, List<MovieShowSeat> showSeats, double price){
        Booking booking = new Booking(user, showSeats, price);
        userBookingMap.putIfAbsent(user.id, new ArrayList<>());
        userBookingMap.get(user.id).add(booking);

        var locks = new ArrayList<ReentrantLock>();
        for(MovieShowSeat showSeat : showSeats){
            if(showSeat.status == SeatStatus.AVAILABLE){
                showSeat.lock.lock();
                locks.add(showSeat.lock);
            }else{
                locks.forEach(ReentrantLock::unlock);
                booking.bookingStatus = BookingStatus.NOT_FULFILLED;
                return BookingStatus.NOT_FULFILLED;
            }
        }

        // Mark all the selected seats unavailable.
        try{
            showSeats.forEach(showSeat -> showSeat.status = SeatStatus.HOLD);

            scheduler.schedule(() -> {
                releaseSeatsIfPending(booking);
            }, 1, TimeUnit.MINUTES);

            PaymentStatus status = paymentService.makePayment(booking);

            if(status == PaymentStatus.SUCCESS){
                booking.bookingStatus = BookingStatus.CONFIRMED;
            }else if(status == PaymentStatus.PENDING){
                //wait. it will process in async way and update the booking status.

            }
            booking.bookingStatus = BookingStatus.CANCELLED;
        }finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return booking.bookingStatus;
    }

    public void releaseSeatsIfPending(Booking booking){
        booking.showSeats.forEach(showSeat -> {
            if(showSeat.status == SeatStatus.HOLD){
                showSeat.status = SeatStatus.AVAILABLE;
            }
        });
    }
}

class PaymentService{
    public PaymentStatus makePayment(Booking booking){
        boolean success = new Random().nextBoolean();
        if(success){
            return PaymentStatus.SUCCESS;
        }else{
            // retry
            return PaymentStatus.FAILURE;
        }
    }
}

class User{
    int id;
    String name;
    User(String name){
        this.name = name;
    }
}

class City{
    int id;
    String name;
    City(String name){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
    }
}

class Movie{
    int id;
    String name;
    int durationInMinutes;
    Movie(String name, int durationInMinutes){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
        this.durationInMinutes = durationInMinutes;
    }
}

class Theatre{
    int id;
    int cityId;
    String address;
    List<Screen> screens;
    List<MovieShowSeat> showSeats;
    Theatre(int cityId, String address){
        this.id = Util.idGenerator.incrementAndGet();
        this.cityId = cityId;
        this.address = address;
        this.screens = new ArrayList<>();
    }

    public void addScreen(Screen screen){
        this.screens.add(screen);
    }

}

class Screen{
    int id;
    String name;
    List<Seat> seats;
    Screen(String name){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
        this.seats = new ArrayList<>();
    }
    public void addSeats(List<Seat> seats){
        this.seats.addAll(seats);
    }
}

class Seat{
    int id;
    String seatNo;
    Seat(String seatNo){
        this.id = Util.idGenerator.incrementAndGet();
        this.seatNo = seatNo;
    }
}

class MovieShowSeat {
    int id;
    int movieId;
    Seat seat;
    LocalDateTime startTime;
    SeatStatus status;
    ReentrantLock lock = new ReentrantLock();

    MovieShowSeat(int movieId, Seat seat, LocalDateTime startTime){
        this.id = Util.idGenerator.incrementAndGet();
        this.movieId = movieId;
        this.seat = seat;
        this.startTime = startTime;
        this.status = SeatStatus.AVAILABLE;
    }
}

class Booking{
    int id;
    List<MovieShowSeat> showSeats;
    User user;
    BookingStatus bookingStatus;
    double price;

    Booking(User user, List<MovieShowSeat> showSeats, double price){
        this.id = Util.idGenerator.incrementAndGet();
        this.bookingStatus = BookingStatus.PENDING;
        this.user = user;
        this.showSeats = showSeats;
        this.price = price;
    }
}

enum SeatStatus{
    AVAILABLE, HOLD, BOOKED
}

enum BookingStatus{
    CONFIRMED, PENDING, CANCELLED, NOT_FULFILLED
}

enum PaymentStatus{
    SUCCESS, PENDING, FAILURE
}

class Util{
    static AtomicInteger idGenerator = new AtomicInteger();
}


