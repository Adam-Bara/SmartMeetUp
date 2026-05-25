package com.example.smartmeetup.data.dummy //mock data for application, build and test the UI (screens, lists, cards) before the app is connected to a real database or server

import com.example.smartmeetup.model.MeetupEvent   //definition of what both files look like aka templates pulling in
import com.example.smartmeetup.model.User
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus

val dummyHost = User(    //creates a single user as user class
    id = 1,
    displayName = "Samira",
    username = "@samira"
)

val dummyParticipants = listOf( //creating a list of participants as user objects grouped
    User(id = 2, displayName = "Ben", username = "@ben"),
    User(id = 3, displayName = "Jonas", username = "@jonas"),
    User(id = 4, displayName = "Mira", username = "@mira"),
    User(id = 5, displayName = "Leo", username = "@leo"),
    User(id = 6, displayName = "Nina", username = "@nina"),
    User(id = 7, displayName = "Tom", username = "@tom"),
    User(id = 8, displayName = "Aylin", username = "@aylin"),
    User(id = 9, displayName = "Felix", username = "@felix"),
    User(id = 10, displayName = "Lea", username = "@lea"),
    User(id = 11, displayName = "Max", username = "@max")
)

val dummyEvents = listOf(    //list of events that EventListScreen will be using for display as cards
    MeetupEvent(
        id = 123,
        title = "Football in the Park",
        category = "Sport",
        previewText = "Casual football match, beginners welcome.",
        description = "A relaxed football meetup in the park. No fixed teams, no pressure, just movement and some company after class.",
        date = "May 24, 2025",
        startTime = "14:00",
        endTime = "16:00",
        locationName = "Stadtpark Gummersbach",
        host = dummyHost,
        participants = dummyParticipants,
        participantLimit = 15,
        status = EventStatus.Ongoing,
        imageType = EventImageType.Park
    ),
    MeetupEvent(
        id = 124,
        title = "Study Session",
        category = "Learning",
        previewText = "Quiet study session for exam prep.",
        description = "A focused study meetup for anyone who wants to work through university tasks together without completely losing their will to live.",
        date = "May 18, 2025",
        startTime = "10:00",
        endTime = "12:00",
        locationName = "Campus Library",
        host = User(id = 12, displayName = "Marlon", username = "@marlon"),
        participants = dummyParticipants.take(4),
        participantLimit = 8,
        status = EventStatus.StartingSoon,
        imageType = EventImageType.Workshop
    ),
    MeetupEvent(
        id = 125,
        title = "Tennis Tournament",
        category = "Sport",
        previewText = "Tournament with students at campus plaza.",
        description = "A tennis tournament for all first semester students.",
        date = "July 18, 2025",
        startTime = "11:00",
        endTime = "12:00",
        locationName = "Campus Tennis Courts",
        host = User(id = 12, displayName = "Marlon", username = "@marlon"),
        participants = dummyParticipants.take(3),
        participantLimit = 8,
        status = EventStatus.StartingSoon,
        imageType = EventImageType.Tennis
    ),

    MeetupEvent(
        id = 126,
        title = "Hiking Trip",
        category = "Sport",
        previewText = "Long hike across the mountains",
        description = "A long hike across the Alps for everyone who feels touching grass",
        date = "Jun 7, 2024",
        startTime = "15:30",
        endTime = "20:30",
        locationName = "Oberberg",
        host = User(id = 3, displayName = "Jonas", username = "@jonas"),
        participants = dummyParticipants.take(2),
        participantLimit = 6,
        status = EventStatus.Archived,
        imageType = EventImageType.Hiking
    ),


    MeetupEvent(
        id = 127,
        title = "Hiking Trip 2",
        category = "Sport",
        previewText = "Another hike across the mountains",
        description = "A new group hike across the Alps for everyone who feels touching grass",
        date = "Jun 10, 2024",
        startTime = "15:30",
        endTime = "20:30",
        locationName = "Oberberg",
        host = User(id = 3, displayName = "Jonas", username = "@jonas"),
        participants = dummyParticipants.take(2),
        participantLimit = 6,
        status = EventStatus.Archived,
        imageType = EventImageType.Hiking
    ),
    MeetupEvent(
        id = 127,
        title = "Hiking Trip 3",
        category = "Sport",
        previewText = "Last hike across the mountains",
        description = "Last group hike this year!",
        date = "Jun 15, 2024",
        startTime = "15:30",
        endTime = "20:30",
        locationName = "Oberberg",
        host = User(id = 3, displayName = "Jonas", username = "@jonas"),
        participants = dummyParticipants.take(2),
        participantLimit = 6,
        status = EventStatus.Cancelled,
        imageType = EventImageType.Hiking
    )
)

val selectedDummyEvent = dummyEvents.first() //selecting a single event, the first, for display
