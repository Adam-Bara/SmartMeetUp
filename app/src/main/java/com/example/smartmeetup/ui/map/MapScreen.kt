package com.example.smartmeetup.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.smartmeetup.model.MeetupEvent
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun MapScreen(
    events: List<MeetupEvent>,
    onCreateEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val radiusInMeters = 3000.0

    var userLocation by remember {
        mutableStateOf<GeoPoint?>(null)
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(context)
        )

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = GeoPoint(
                        location.latitude,
                        location.longitude
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { viewContext ->
                MapView(viewContext).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    controller.setZoom(15.0)

                    // Fallback-Startpunkt, falls Standort noch nicht geladen ist
                    controller.setCenter(
                        GeoPoint(52.5200, 13.4050)
                    )
                }
            },
            update = { mapView ->
                mapView.overlays.clear()

                val currentLocation = userLocation

                if (currentLocation != null) {
                    mapView.controller.setCenter(currentLocation)

                    val radiusCircle = Polygon().apply {
                        points = Polygon.pointsAsCircle(
                            currentLocation,
                            radiusInMeters
                        )
                        fillColor = android.graphics.Color.argb(
                            45,
                            3,
                            104,
                            246
                        )
                        strokeColor = android.graphics.Color.rgb(
                            3,
                            104,
                            246
                        )
                        strokeWidth = 4f
                    }

                    mapView.overlays.add(radiusCircle)

                    val userMarker = Marker(mapView).apply {
                        position = currentLocation
                        title = "Dein Standort"
                        setAnchor(
                            Marker.ANCHOR_CENTER,
                            Marker.ANCHOR_BOTTOM
                        )
                    }

                    mapView.overlays.add(userMarker)

                    events
                        .filter { event ->
                            val eventPoint = GeoPoint(
                                event.latitude,
                                event.longitude
                            )

                            distanceBetween(
                                currentLocation,
                                eventPoint
                            ) <= radiusInMeters
                        }
                        .forEach { event ->
                            val eventMarker = Marker(mapView).apply {
                                position = GeoPoint(
                                    event.latitude,
                                    event.longitude
                                )
                                title = event.title
                                snippet = event.category
                                setAnchor(
                                    Marker.ANCHOR_CENTER,
                                    Marker.ANCHOR_BOTTOM
                                )
                            }

                            mapView.overlays.add(eventMarker)
                        }
                }

                mapView.invalidate()
            }
        )

        CreateEventButton(
            onClick = onCreateEventClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 0.dp, bottom = 20.dp)
        )
    }
}

@Composable
private fun CreateEventButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(180.dp)
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0368F6)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Event erstellen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

private fun distanceBetween(
    start: GeoPoint,
    end: GeoPoint
): Double {
    val earthRadius = 6371000.0

    val startLat = Math.toRadians(start.latitude)
    val endLat = Math.toRadians(end.latitude)
    val deltaLat = Math.toRadians(end.latitude - start.latitude)
    val deltaLon = Math.toRadians(end.longitude - start.longitude)

    val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
            cos(startLat) * cos(endLat) *
            sin(deltaLon / 2) * sin(deltaLon / 2)

    val c = 2 * atan2(
        sqrt(a),
        sqrt(1 - a)
    )

    return earthRadius * c
}

// Preview erstmal entfernt, könnte probleme machen