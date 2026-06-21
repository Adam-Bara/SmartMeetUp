// File Purpose: Central point for accessing device location data, abstracting Fused Location Provider.
// Communication: MapViewModel, MapScreen, and EventViewModels for location-based features.
// Owner: Rina

package com.example.smartmeetup.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

// This repository acts as the central point for accessing device location data.
// It abstracts the Google Play Services Fused Location Provider, allowing other
// parts of the app (like MapScreen or EventViewModels) to request coordinates
// without handling complex permission checks or client initialization themselves.
class LocationRepository(
    context: Context
) {
    private val applicationContext = context.applicationContext

    // The primary entry point for interacting with the Fused Location Provider.
    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

    // Helper function to check if the user has granted either fine or coarse location permissions.
    // This is used by UI components to decide whether to show location-based features.
    fun hasLocationPermission(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationGranted || coarseLocationGranted
    }

    // Attempts to retrieve the most accurate current location of the device.
    // It uses PRIORITY_HIGH_ACCURACY for precision, which is essential for map-based features.
    // If a fresh location isn't available, it automatically falls back to the last known location.
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onSuccess: (Location?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onSuccess(null)
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            .addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    onSuccess(currentLocation)
                } else {
                    // Fallback if current location request returns null
                    loadLastKnownLocation(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            }
            .addOnFailureListener { exception ->
                // Fallback if the request itself fails (e.g., location services disabled)
                loadLastKnownLocation(
                    onSuccess = onSuccess,
                    onError = { onError(exception) }
                )
            }
    }

    // A faster, lower-power way to get a location if a fresh fix isn't strictly required.
    // It retrieves the last location cached by the system from previous requests by any app.
    @SuppressLint("MissingPermission")
    private fun loadLastKnownLocation(
        onSuccess: (Location?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onSuccess(location)
            }
            .addOnFailureListener { exception ->
                onError(exception)
        }
    }
}
