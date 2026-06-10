package com.example.smartmeetup.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

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
    fun getLastKnownLocation(
        onSuccess: (Location?) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onSuccess(null)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                onSuccess(location)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}