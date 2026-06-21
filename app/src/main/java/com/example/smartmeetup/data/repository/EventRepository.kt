// File Purpose: Repository for managing access to MeetupEvent data, currently fetching from DummyEvents.
// Communication: DummyEvents, EventViewModel, MeetupEvent.
// Owner: Daria Zecha

package com.example.smartmeetup.data.repository

import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.MeetupEvent

/**
 * Repository responsible for managing and providing access to [MeetupEvent] data.
 */
class EventRepository {

    fun getAllEvents(): List<MeetupEvent> {
        return dummyEvents
    }
}