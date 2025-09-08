Functional Requirements:
 - Admin can add/remove meeting rooms
 - Organiser can check for availablity
 - Organiser can book the meeting. (interval, list of participants, meetingRoom, agenda)
 - Overlapping of intervals not allowed for same meeting room.
 - Organiser can update meeting with add/remove participant, update agenda..etc
 - Organiser can cancel the meeting.
Below the line if time permits:
 - Notification Send to users for booking,cancel, modification (Oberver pattern)

MeetingScheduler{
    // To keep track of all the meetings scheduled for meeting room. to get the availablity faster
    Map<MeetingRoom, TreeSet<Meeting>> bookings;
    
    checkAvaialblity(noOfParticipants, interval) : List<MeetingRoom>
    bookMeeting(interval, meetingRoom, List<Participants>) : bookingStatus
    cancelMeeting(meeting);
    updateMeeting(agenda, List<Participants> toBeRemoved, List<Participants> toBeAdded)
    
}

NotificationService{

}

Participant
 - id
 - name
 - Calender
Calender
 - participantId
 - TreeSet<Meeting> : sorted order by date

MeetingRoom
 - id
 - capacity
 - name
 - location_details : like floor

Interval
 - startTime - date/time
 - endTime - date/time

Meeting
 - id
 - Interval
 - MeetingRoom
 - agenda
 - Map<Participant, RSVPStatus> participants
 - organiser : Participant

NotificationRequest
 - participantId

enum RSVPStatus{
    INVITED, ACCEPTED, DECLINED
}

enum MeetingRoomStatus{
    AVAILABLE, OCCUPIED
}