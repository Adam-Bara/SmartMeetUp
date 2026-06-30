// File Purpose: ViewModel for managing database-backed event state, selected events,
// joining/leaving events, and creating events from the editable CreateEventScreen form.
// Communication: EventRepository, SmartMeetupDatabase, MeetupEvent, CreateEventFormState.
// Owner: Kaida  / Milestone 4 Room integration

package com.example.smartmeetup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmeetup.data.local.SmartMeetupDatabase
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User
import com.example.smartmeetup.ui.create.CreateEventFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class EventViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val currentUser = User(
        id = 999,
        displayName = "Kaida",
        username = "@kaida"
    )

    private val eventRepository: EventRepository = EventRepository(
        SmartMeetupDatabase.getDatabase(application).eventDao()
    )

    private val _selectedEventId = MutableStateFlow<Int?>(null)

    private val allEvents: StateFlow<List<MeetupEvent>> = eventRepository
        .observeAllEventsPersisted()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val joinedEvents: StateFlow<List<MeetupEvent>> = allEvents
        .map { events ->
            events.filter { event ->
                event.host.id == currentUser.id ||
                        event.participants.any { participant ->
                            participant.id == currentUser.id
                        }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val selectedEvent: StateFlow<MeetupEvent?> = combine(
        allEvents,
        _selectedEventId
    ) { events, selectedEventId ->
        events.firstOrNull { event ->
            event.id == selectedEventId
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            eventRepository.seedDummyEventsIfEmpty()
        }
    }

    fun selectEvent(eventId: Int) {
        _selectedEventId.value = eventId
    }

    fun joinEvent(eventId: Int, user: User = currentUser) {
        viewModelScope.launch {
            val updatedEvent = eventRepository.joinEventPersisted(
                eventId = eventId,
                user = user
            )

            if (updatedEvent != null) {
                _selectedEventId.value = updatedEvent.id
            }
        }
    }

    fun leaveEvent(eventId: Int, user: User = currentUser) {
        viewModelScope.launch {
            eventRepository.leaveEventPersisted(
                eventId = eventId,
                user = user
            )

            if (_selectedEventId.value == eventId) {
                _selectedEventId.value = null
            }
        }
    }

    fun createEventFromForm(formState: CreateEventFormState) {
        viewModelScope.launch {
            val nextId = (
                    eventRepository.getAllEventsPersisted().maxOfOrNull { event ->
                        event.id
                    } ?: 0
                    ) + 1

            val newEvent = MeetupEvent(
                id = nextId,
                title = formState.title,
                category = formState.category,
                previewText = formState.description.take(90),
                description = formState.description,
                date = formState.date,
                startTime = formState.startTime,
                endTime = formState.endTime,
                locationName = formState.locationName,
                latitude = 51.026,
                longitude = 7.562,
                host = currentUser,
                participants = listOf(currentUser),
                participantLimit = formState.participantLimit,
                status = EventStatus.Upcoming,
                imageType = EventImageType.Workshop
            )

            val persistedEvent = eventRepository.createEventPersisted(newEvent)
            _selectedEventId.value = persistedEvent.id
        }
    }


    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.deleteEventPersisted(eventId)

            if (_selectedEventId.value == eventId) {
                _selectedEventId.value = null
            }
        }
    }

    fun clearSelectedEvent() {
        _selectedEventId.value = null
    }
}