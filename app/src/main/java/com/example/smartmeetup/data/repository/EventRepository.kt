// File Purpose: Repository for managing access to MeetupEvent data.
// Communication: DummyEvents, EventViewModel, MeetupEvent, Room database layer.
// Owner: Daria Zecha

package com.example.smartmeetup.data.repository

import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.data.local.EventDao
import com.example.smartmeetup.data.local.toEntity
import com.example.smartmeetup.data.local.toMeetupEvent
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository responsible for managing and providing access to [MeetupEvent] data.
 *
 * Milestone 3 used an in-memory mock repository.
 * Milestone 4 adds Room-based local persistence while keeping the old in-memory
 * functions temporarily available until the ViewModel is fully migrated.
 */
class EventRepository(
    private val eventDao: EventDao? = null
) {

    private val events = dummyEvents.toMutableList()

    // -------------------------------------------------------------------------
    // Legacy in-memory functions from Milestone 3.
    // These keep the current app working until EventViewModel is migrated.
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Room-backed functions for Milestone 4.
    // These will become the real source of truth after ViewModel migration.
    // -------------------------------------------------------------------------

    fun observeAllEventsPersisted(): Flow<List<MeetupEvent>> {
        val dao = requireEventDao()

        return dao.observeAllEvents().map { eventEntities ->
            eventEntities.map { eventEntity ->
                eventEntity.toMeetupEvent()
            }
        }
    }

    suspend fun seedDummyEventsIfEmpty() {
        val dao = requireEventDao()

        if (dao.getEventCount() == 0) {
            dao.insertEvents(
                dummyEvents.map { event ->
                    event.toEntity()
                }
            )
        }
    }

    suspend fun getAllEventsPersisted(): List<MeetupEvent> {
        val dao = requireEventDao()

        return dao.getAllEventsOnce().map { eventEntity ->
            eventEntity.toMeetupEvent()
        }
    }

    suspend fun getEventByIdPersisted(eventId: Int): MeetupEvent? {
        val dao = requireEventDao()

        return dao.getEventById(eventId)?.toMeetupEvent()
    }

    suspend fun joinEventPersisted(eventId: Int, user: User): MeetupEvent? {
        val dao = requireEventDao()
        val event = dao.getEventById(eventId)?.toMeetupEvent() ?: return null

        val userAlreadyJoined = event.participants.any { participant ->
            participant.id == user.id
        }

        if (userAlreadyJoined || event.participants.size >= event.participantLimit) {
            return event
        }

        val updatedEvent = event.copy(
            participants = event.participants + user
        )

        dao.updateEvent(updatedEvent.toEntity())

        return updatedEvent
    }

    suspend fun leaveEventPersisted(eventId: Int, user: User): MeetupEvent? {
        val dao = requireEventDao()
        val event = dao.getEventById(eventId)?.toMeetupEvent() ?: return null

        val updatedEvent = event.copy(
            participants = event.participants.filterNot { participant ->
                participant.id == user.id
            }
        )

        dao.updateEvent(updatedEvent.toEntity())

        return updatedEvent
    }

    suspend fun createEventPersisted(event: MeetupEvent): MeetupEvent {
        val dao = requireEventDao()
        val conflictSafeEvent = createConflictSafeEvent(event)

        dao.insertEvent(conflictSafeEvent.toEntity())

        return conflictSafeEvent
    }

    suspend fun deleteEventPersisted(eventId: Int) {
        val dao = requireEventDao()

        dao.deleteEventById(eventId)
    }

    private suspend fun createConflictSafeEvent(event: MeetupEvent): MeetupEvent {
        val dao = requireEventDao()
        var conflictSafeEvent = event

        var nextId = conflictSafeEvent.id
        while (dao.eventIdExists(nextId)) {
            nextId += 1
        }

        if (nextId != conflictSafeEvent.id) {
            conflictSafeEvent = conflictSafeEvent.copy(id = nextId)
        }

        var title = conflictSafeEvent.title
        var counter = 2

        while (
            dao.logicalDuplicateExists(
                title = title,
                date = conflictSafeEvent.date,
                startTime = conflictSafeEvent.startTime,
                locationName = conflictSafeEvent.locationName
            )
        ) {
            title = "${conflictSafeEvent.title} ($counter)"
            counter += 1
        }

        return conflictSafeEvent.copy(title = title)
    }

    private fun requireEventDao(): EventDao {
        return checkNotNull(eventDao) {
            "EventDao is required for Room-backed repository functions."
        }
    }
}