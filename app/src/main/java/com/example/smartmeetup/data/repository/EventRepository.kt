package com.example.smartmeetup.data.repository

import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.MeetupEvent

class EventRepository {

    fun getAllEvents(): List<MeetupEvent> {
        return dummyEvents
    }
}