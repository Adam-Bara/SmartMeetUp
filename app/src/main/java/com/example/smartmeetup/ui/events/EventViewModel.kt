package com.example.smartmeetup.ui.events

//EventViewModel belongs to the whole events feature, not to one specific screen
//provides state/actions for multiple event screens -> sits between fake data layer and UI screens
//is the state manager as It takes the raw event data from the repository and turns it into UI-ready state. That state is called EventUiState

import androidx.lifecycle.ViewModel
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
}
