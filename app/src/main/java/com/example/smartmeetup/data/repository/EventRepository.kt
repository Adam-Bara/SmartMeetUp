// File Purpose: Repository for managing access to MeetupEvent data, currently using in-memory dummy data.
// Communication: DummyEvents, EventViewModel, MeetupEvent.
// Owner: Daria Zecha

package com.example.smartmeetup.data.repository

import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User

/**
 * Repository responsible for managing and providing access to [MeetupEvent] data.
 *
 * For Milestone 3 this is still an in-memory mock repository:
 * - data starts from DummyEvents
 * - joining/leaving/creating updates the local list
 * - nothing is persisted permanently yet
 */
class EventRepository {

    private val events = dummyEvents.toMutableList()

    fun getAllEvents(): List<MeetupEvent> {
        return events
    }

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

        if (eventIndex == -1) {
            return null
        }

        val event = events[eventIndex]

        val userAlreadyJoined = event.participants.any { participant ->
            participant.id == user.id
        }

        if (userAlreadyJoined || event.participants.size >= event.participantLimit) {
            return event
        }

        val updatedEvent = event.copy(
            participants = event.participants + user
        )

        events[eventIndex] = updatedEvent

        return updatedEvent
    }

    fun leaveEvent(eventId: Int, user: User): MeetupEvent? {
        val eventIndex = events.indexOfFirst { event ->
            event.id == eventId
        }

        if (eventIndex == -1) {
            return null
        }

        val event = events[eventIndex]

        val updatedEvent = event.copy(
            participants = event.participants.filterNot { participant ->
                participant.id == user.id
            }
        )

        events[eventIndex] = updatedEvent

        return updatedEvent
    }

    fun createEvent(event: MeetupEvent): MeetupEvent {
        events.add(0, event)
        return event
    }
}