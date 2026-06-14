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

class LocationRepository(
    context: Context
) {
    private val applicationContext = context.applicationContext

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(applicationContext)

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
                    loadLastKnownLocation(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                }
            }
            .addOnFailureListener { exception ->
                loadLastKnownLocation(
                    onSuccess = onSuccess,
                    onError = { onError(exception) }
                )
            }
    }

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