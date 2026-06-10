package com.example.smartmeetup.ui.map

import com.example.smartmeetup.model.MeetupEvent

data class MapUiState(
    val events: List<MeetupEvent> = emptyList(),
    val nearbyEvents: List<MeetupEvent> = emptyList(),
    val userLatitude: Double? = null,
    val userLongitude: Double? = null,
    val radiusInMeters: Double = 3000.0,
    val hasLocationPermission: Boolean = false,
    val isLoadingLocation: Boolean = false,
    val locationErrorMessage: String? = null
)