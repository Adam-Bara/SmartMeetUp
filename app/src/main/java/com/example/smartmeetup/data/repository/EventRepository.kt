package com.example.smartmeetup.data.repository

//this file it takes the existing dummyEvents, stores them in a mutable in-memory list, and gives the
// rest of the app clean functions to read, join, leave, and create events. Nothing is saved permanently yet;

import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User

class EventRepository {

    private val events = dummyEvents.toMutableList()

    fun getEvents(): List<MeetupEvent> {
        return events
    }

    fun getEventById(eventId: Int): MeetupEvent? {
        return events.find { event ->
            event.id == eventId
        }
    }

    fun joinEvent(eventId: Int, user: User): MeetupEvent? {
        val eventIndex = events.indexOfFirst { event ->
            event.id == eventId
        }

        if (eventIndex == -1) return null

        val event = events[eventIndex]

        val userAlreadyJoined = event.participants.any { participant ->
            participant.id == user.id
        }

        if (userAlreadyJoined) return event

        val eventIsFull = event.participants.size >= event.participantLimit

        if (eventIsFull) return event

        val updatedEvent = event.copy(
            participants = event.participants + user
        )

        events[eventIndex] = updatedEvent

        return updatedEvent
    }

    fun leaveEvent(eventId: Int, userId: Int): MeetupEvent? {
        val eventIndex = events.indexOfFirst { event ->
            event.id == eventId
        }

        if (eventIndex == -1) return null

        val event = events[eventIndex]

        val updatedEvent = event.copy(
            participants = event.participants.filterNot { participant ->
                participant.id == userId
            }
        )

        events[eventIndex] = updatedEvent

        return updatedEvent
    }

    fun createEvent(event: MeetupEvent): MeetupEvent {
        events.add(event)
        return event
    }
}
