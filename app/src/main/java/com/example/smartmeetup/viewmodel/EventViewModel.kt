package com.example.smartmeetup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.MeetupEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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

    fun leaveEvent(eventId: Int) {
        _joinedEvents.value = _joinedEvents.value.filterNot { event ->
            event.id == eventId
        }

        if (_selectedEventId.value == eventId) {
            _selectedEventId.value = null
        }
    }

    fun clearSelectedEvent() {
        _selectedEventId.value = null
    }
}