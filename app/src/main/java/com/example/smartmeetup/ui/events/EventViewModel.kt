package com.example.smartmeetup.ui.events

import androidx.lifecycle.ViewModel
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// EventViewModel belongs to the whole events feature, not to one specific screen.
// It sits between the fake data layer and the UI screens.
// It turns repository data into UI-ready state and handles event actions.
data class EventUiState(
    val events: List<MeetupEvent> = emptyList(),
    val selectedEvent: MeetupEvent? = null,
    val errorMessage: String? = null
)

class EventViewModel(
    private val eventRepository: EventRepository = EventRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        EventUiState(
            events = eventRepository.getEvents()
        )
    )

    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    fun selectEvent(eventId: Int) {
        val selectedEvent = eventRepository.getEventById(eventId)

        _uiState.value = _uiState.value.copy(
            selectedEvent = selectedEvent,
            errorMessage = if (selectedEvent == null) "Event not found" else null
        )
    }

    fun joinEvent(eventId: Int, user: User) {
        val updatedEvent = eventRepository.joinEvent(eventId, user)

        _uiState.value = _uiState.value.copy(
            events = eventRepository.getEvents(),
            selectedEvent = updatedEvent,
            errorMessage = if (updatedEvent == null) "Could not join event" else null
        )
    }

    fun leaveEvent(eventId: Int, userId: Int) {
        val updatedEvent = eventRepository.leaveEvent(eventId, userId)

        _uiState.value = _uiState.value.copy(
            events = eventRepository.getEvents(),
            selectedEvent = updatedEvent,
            errorMessage = if (updatedEvent == null) "Could not leave event" else null
        )
    }

    fun createEvent(event: MeetupEvent) {
        val createdEvent = eventRepository.createEvent(event)

        _uiState.value = _uiState.value.copy(
            events = eventRepository.getEvents(),
            selectedEvent = createdEvent,
            errorMessage = null
        )
    }

    // Creates a temporary event from the static CreateEventScreen.
    // This gives the publish button real behavior before the form fields are editable.
    fun createMockEventFromForm() {
        val nextEventId = (_uiState.value.events.maxOfOrNull { event ->
            event.id
        } ?: 0) + 1

        val host = User(
            id = 99,
            displayName = "Lucas",
            username = "@lucas"
        )

        val createdEvent = MeetupEvent(
            id = nextEventId,
            title = "New Meetup Event",
            category = "Sport",
            previewText = "A newly created event from the create screen.",
            description = "This event was created from the CreateEventScreen as a first Milestone 3 mock implementation.",
            date = "June 15, 2026",
            startTime = "13:00",
            endTime = "15:00",
            locationName = "Campus Gummersbach",
            latitude = 51.0236,
            longitude = 7.5632,
            host = host,
            participants = listOf(host),
            participantLimit = 15,
            status = EventStatus.Upcoming,
            imageType = EventImageType.Park
        )

        createEvent(createdEvent)
    }
}