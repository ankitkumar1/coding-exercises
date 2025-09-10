package ankit.lld.meetingscheduler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MeetingScheduler {
    public static void main(String[] args) {
        List<MeetingRoom> meetingRooms = List.of(new MeetingRoom(10, "M5_1", "5th Floor"),
                new MeetingRoom(15, "M4_1", "4th Floor"),
                new MeetingRoom(5, "M5_2", "5th Floor"));
        MeetingScheduler meetingScheduler = MeetingScheduler.getInstance();
        meetingScheduler.addMeetingRooms(meetingRooms);

        Participant p1 = new Participant("Ankit");
        Participant p2 = new Participant("Vicky");
        Participant p3 = new Participant("AK");
        Participant p4 = new Participant("AKumar");
        Participant p5 = new Participant("Ram");

        System.out.println("@@@@@@@@@@ Test Case: 1 @@@@@@@@");
        System.out.println();
        Interval interval = new Interval(LocalDateTime.of(2025, 9, 7, 10, 30),
                LocalDateTime.of(2025, 9, 7, 11, 30));
        List<MeetingRoom> availableRooms = meetingScheduler.getAvaialbleRooms(interval);
        System.out.println(availableRooms);
        Optional<Meeting> meeting = meetingScheduler.bookMeeting(p1, interval, availableRooms.getFirst(), "Introductory Meeting",
                List.of(p2, p3, p4));
        meeting.ifPresent(value -> System.out.println(value.id));
        System.out.println(p2.calender.meetings);

        System.out.println();
        System.out.println();
        System.out.println("@@@@@@@@@@ Test Case: 2 @@@@@@@@");
        System.out.println();
        Interval interval2 = new Interval(LocalDateTime.of(2025, 9, 7, 11, 30),
                LocalDateTime.of(2025, 9, 7, 12, 30));
        List<MeetingRoom> availableRooms1 = meetingScheduler.getAvaialbleRooms(interval2);
        System.out.println(availableRooms1);
        Optional<Meeting> meeting2 = meetingScheduler.bookMeeting(p5, interval2, availableRooms.getFirst(), "Introductory Meeting",
                List.of(p2, p5));
        meeting2.ifPresent(value -> System.out.println(value.id));
        System.out.println(p2.calender.meetings);

    }

    private final List<MeetingRoom> meetingRooms;
    private final Map<Integer, TreeSet<Meeting>> upComingMeetings;

    private MeetingScheduler(){
        this.meetingRooms = new CopyOnWriteArrayList<>();
        this.upComingMeetings = new ConcurrentHashMap<>();
    }

    private static class Holder{
        private static final MeetingScheduler INSTANCE = new MeetingScheduler();
    }

    public static MeetingScheduler getInstance(){
        return Holder.INSTANCE;
    }

    public void addMeetingRooms(List<MeetingRoom> rooms){
        this.meetingRooms.addAll(rooms);
    }

    public List<MeetingRoom> getAvaialbleRooms(Interval interval){
        List<MeetingRoom> rooms = new ArrayList<>();
        for(MeetingRoom room : meetingRooms){
            TreeSet<Meeting> scheduledMeetings = upComingMeetings.get(room.id);
            if(scheduledMeetings == null || scheduledMeetings.isEmpty()){
                rooms.add(room);
                continue;
            }
            boolean isOverlapping = isOverlapping(interval, scheduledMeetings);
            if(!isOverlapping){
                rooms.add(room);
            }
        }
        rooms.sort(Comparator.comparing(MeetingRoom::getCapacity).reversed());
        return rooms;
    }

    public  Optional<Meeting> bookMeeting(Participant organiser, Interval interval, MeetingRoom meetingRoom,String agenda,  List<Participant> participants){
        meetingRoom.lock.lock();
        if(!isMeetingRoomAvailable(meetingRoom, interval)){
            System.out.println("Uff ohh.. Just missed. Someone else booked this Meeting room!");
            meetingRoom.lock.unlock();
            return Optional.empty();
        }
       try{
           Meeting meeting = new Meeting(interval, meetingRoom, agenda, organiser, participants);
           this.upComingMeetings.putIfAbsent(meetingRoom.id, new TreeSet<>(Comparator.comparing(m -> m.interval.startTime())));
           this.upComingMeetings.get(meetingRoom.id).add(meeting);
           participants.forEach((p) -> p.calender.meetings.add(meeting));
           System.out.println("Meeting room booked successfully!");
           return Optional.of(meeting);
        }finally {
            meetingRoom.lock.unlock();
        }
    }

    public void cancelMeeting(Meeting meeting){

    }

    public void updateMeeting(String agenda, List<Participant> toBeRemoved, List<Participant> toBeAdded){

    }

    private boolean isMeetingRoomAvailable(MeetingRoom meetingRoom, Interval interval){
        TreeSet<Meeting> availability = upComingMeetings.get(meetingRoom.id);
        if(availability == null || availability.isEmpty()){
            return true;
        }
        return !isOverlapping(interval, availability);
    }

    private static boolean isOverlapping(Interval interval, TreeSet<Meeting> scheduledMeetings) {
        return scheduledMeetings.stream().anyMatch(meeting -> {
            Interval meetingInterval = meeting.interval;

            return meetingInterval.equals(interval) || (interval.startTime().isAfter(meetingInterval.startTime()) &&
                    interval.startTime().isBefore(meetingInterval.endTime())) ||
                    interval.endTime().isAfter(meetingInterval.startTime()) &&
                            interval.endTime().isBefore(meetingInterval.endTime());
        });
    }

}

class Participant{
    int id;
    String name;
    ParticipantCalender calender;
    Participant(String name){
        this.id = Util.idGenerator.incrementAndGet();
        this.name = name;
        this.calender = new ParticipantCalender(this.id);
    }
}

class ParticipantCalender{
    int participantId;
    NavigableSet<Meeting> meetings;
    ParticipantCalender(int participantId){
        this.participantId = participantId;
        this.meetings = new ConcurrentSkipListSet<>(
                Comparator.comparing((Meeting m) -> m.interval.startTime()).thenComparing(m -> m.id));
    }
}
class MeetingRoom{
    int id;
    int capacity;
    String name;
    String locationDetails;
    ReentrantLock lock = new ReentrantLock();
    MeetingRoom(int capacity, String name, String locationDetails){
        this.id = Util.idGenerator.incrementAndGet();
        this.capacity = capacity;
        this.name = name;
        this.locationDetails = locationDetails;
    }
    public int getCapacity(){
        return this.capacity;
    }

    @Override
    public String toString(){
        return "{Id: "+id+",Capacity: "+capacity+",Name: "+name+"}";
    }
}

record Interval(LocalDateTime startTime, LocalDateTime endTime){
    public Interval {
        if (startTime.isAfter(endTime)) throw new IllegalArgumentException("end must be after start");
    }
}

class Meeting{
    int id;
    Interval interval;
    MeetingRoom meetingRoom;
    String agenda;
    Map<Participant, RSVPStatus> participantRSVPStatusMap;
    Participant organiser;

    Meeting(Interval interval, MeetingRoom meetingRoom, String agenda,
            Participant organiser, List<Participant> participants){
        this.id = Util.idGenerator.incrementAndGet();
        this.interval = interval;
        this.meetingRoom = meetingRoom;
        this.agenda = agenda;
        this.organiser = organiser;
        this.participantRSVPStatusMap = new ConcurrentHashMap<>();
        this.participantRSVPStatusMap.put(organiser, RSVPStatus.ACCEPTED);
        participants.forEach(participant -> {
            this.participantRSVPStatusMap.put(participant, RSVPStatus.INVITED);
        });
    }

    @Override
    public String toString(){
        return "{Meeting Agenda : "+agenda+": Starts at: "+interval.startTime()+": End Time : "+interval.endTime()+"}";
    }
}

enum RSVPStatus{
    INVITED, ACCEPTED, DECLINED
}

class Util{
    static AtomicInteger idGenerator = new AtomicInteger();
}
