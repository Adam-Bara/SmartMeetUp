package com.example.smartmeetup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.smartmeetup.data.location.LocationRepository
import com.example.smartmeetup.data.repository.EventRepository
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.ui.map.MapUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MapViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository(application)
    private val eventRepository = EventRepository()

    private val _uiState = MutableStateFlow(
        MapUiState(
            events = eventRepository.getAllEvents()
        )
    )

    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        val hasPermission = locationRepository.hasLocationPermission()

        _uiState.update { currentState ->
            val events = eventRepository.getAllEvents()

            currentState.copy(
                events = events,
                hasLocationPermission = hasPermission
            )
        }

        if (hasPermission) {
            loadUserLocation()
        }
    }

    fun onLocationPermissionResult(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                hasLocationPermission = granted,
                locationErrorMessage = if (granted) {
                    null
                } else {
                    "Standortberechtigung wurde nicht erlaubt."
                }
            )
        }

        if (granted) {
            loadUserLocation()
        }
    }

    fun refreshUserLocation() {
        if (!locationRepository.hasLocationPermission()) {
            _uiState.update { currentState ->
                currentState.copy(
                    hasLocationPermission = false,
                    isLoadingLocation = false,
                    locationErrorMessage = "Standortberechtigung wurde nicht erlaubt."
                )
            }
            return
        }

        loadUserLocation()
    }

    private fun loadUserLocation() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoadingLocation = true,
                locationErrorMessage = null
            )
        }

        locationRepository.getCurrentLocation(
            onSuccess = { location ->
                if (location == null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoadingLocation = false,
                            locationErrorMessage = "Standort konnte nicht geladen werden."
                        )
                    }
                    return@getCurrentLocation
                }

                _uiState.update { currentState ->
                    val nearbyEvents = filterNearbyEvents(
                        events = currentState.events,
                        userLatitude = location.latitude,
                        userLongitude = location.longitude,
                        radiusInMeters = currentState.radiusInMeters
                    )

                    currentState.copy(
                        userLatitude = location.latitude,
                        userLongitude = location.longitude,
                        nearbyEvents = nearbyEvents,
                        isLoadingLocation = false,
                        locationErrorMessage = null
                    )
                }
            },
            onError = { exception ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoadingLocation = false,
                        locationErrorMessage = exception.message ?: "Standort konnte nicht geladen werden."
                    )
                }
            }
        )
    }

    private fun filterNearbyEvents(
        events: List<MeetupEvent>,
        userLatitude: Double,
        userLongitude: Double,
        radiusInMeters: Double
    ): List<MeetupEvent> {
        return events.filter { event ->
            distanceBetween(
                startLatitude = userLatitude,
                startLongitude = userLongitude,
                endLatitude = event.latitude,
                endLongitude = event.longitude
            ) <= radiusInMeters
        }
    }

    private fun distanceBetween(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): Double {
        val earthRadius = 6371000.0

        val startLat = Math.toRadians(startLatitude)
        val endLat = Math.toRadians(endLatitude)
        val deltaLat = Math.toRadians(endLatitude - startLatitude)
        val deltaLon = Math.toRadians(endLongitude - startLongitude)

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(startLat) * cos(endLat) *
                sin(deltaLon / 2) * sin(deltaLon / 2)

        val c = 2 * atan2(
            sqrt(a),
            sqrt(1 - a)
        )

        return earthRadius * c
    }
}