package com.example.smartmeetup.data.dummy

import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User

val dummyHost = User(
    id = 1,
    displayName = "Samira",
    username = "@samira"
)

val dummyParticipants = listOf(
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

val dummyEvents = listOf(
    MeetupEvent(
        id = 123,
        title = "Football in the Park",
        category = "Sport",
        previewText = "Casual football match, beginners welcome.",
        description = "A relaxed football meetup in the park. No fixed teams, no pressure, just movement and some company after class.",
        startTime = "14:00",
        endTime = "16:00",
        locationName = "Stadtpark Gummersbach",
        host = dummyHost,
        participants = dummyParticipants,
        participantLimit = 15
    ),
    MeetupEvent(
        id = 124,
        title = "Study Session",
        category = "Learning",
        previewText = "Quiet study session for exam prep.",
        description = "A focused study meetup for anyone who wants to work through university tasks together without completely losing their will to live.",
        startTime = "10:00",
        endTime = "12:00",
        locationName = "Campus Library",
        host = User(id = 12, displayName = "Marlon", username = "@marlon"),
        participants = dummyParticipants.take(4),
        participantLimit = 8
    ),
    MeetupEvent(
        id = 125,
        title = "Coffee After Lecture",
        category = "Social",
        previewText = "Short coffee break near campus.",
        description = "A spontaneous café meetup after lecture for anyone who wants to talk, recharge, or stare meaningfully at caffeine.",
        startTime = "15:30",
        endTime = "16:30",
        locationName = "Café Central",
        host = User(id = 13, displayName = "Elena", username = "@elena"),
        participants = dummyParticipants.take(3),
        participantLimit = 6
    )
)

val selectedDummyEvent = dummyEvents.first()
