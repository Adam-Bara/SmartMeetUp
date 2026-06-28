// File Purpose: ViewModel for managing event-related state, such as the event list, selected event,
// joining/leaving events, and creating a mock event.
// Communication: EventRepository, MeetupEvent, EventListScreen, EventJoinedScreen, CreateEventScreen.
// Owner: Daria Zecha / Rescue integration

package com.example.smartmeetup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val MOCK_CREATE_EVENT_DELAY_MS = 300L

class EventViewModel(
    private val eventRepository: EventRepository = EventRepository()
) : ViewModel() {

    private val _joinedEvents = MutableStateFlow(eventRepository.getAllEvents())
    val joinedEvents: StateFlow<List<MeetupEvent>> = _joinedEvents

    private val _selectedEventId = MutableStateFlow<Int?>(null)

    val selectedEvent: StateFlow<MeetupEvent?> =
        combine(
            _joinedEvents,
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

    fun selectEvent(eventId: Int) {
        _selectedEventId.value = eventId
    }

    fun joinEvent(eventId: Int, user: User) {
        val updatedEvent = eventRepository.joinEvent(eventId, user)

        if (updatedEvent != null) {
            _joinedEvents.value = eventRepository.getAllEvents()
            _selectedEventId.value = updatedEvent.id
        }
    }

    fun leaveEvent(eventId: Int) {
        _joinedEvents.value = _joinedEvents.value.filterNot { event ->
            event.id == eventId
        }

        if (_selectedEventId.value == eventId) {
            _selectedEventId.value = null
        }
    }

    fun createMockEventFromForm() {
        viewModelScope.launch {
            delay(MOCK_CREATE_EVENT_DELAY_MS)

            val nextId = (_joinedEvents.value.maxOfOrNull { event ->
                event.id
            } ?: 0) + 1

            val currentUser = User(
                id = 999,
                displayName = "Kaida",
                username = "@kaida"
            )

            val newEvent = MeetupEvent(
                id = nextId,
                title = "New Meetup",
                category = "Study",
                previewText = "A newly created mock event.",
                description = "This event was created from the CreateEventScreen as a mock Milestone 3 flow.",
                date = "Today",
                startTime = "18:00",
                endTime = "20:00",
                locationName = "Campus Gummersbach",
                latitude = 51.026,
                longitude = 7.562,
                host = currentUser,
                participants = listOf(currentUser),
                participantLimit = 8,
                status = EventStatus.Upcoming,
                imageType = EventImageType.Workshop
            )

            eventRepository.createEvent(newEvent)
            _joinedEvents.value = eventRepository.getAllEvents()
            _selectedEventId.value = newEvent.id
        }
    }

    fun clearSelectedEvent() {
        _selectedEventId.value = null
    }
}