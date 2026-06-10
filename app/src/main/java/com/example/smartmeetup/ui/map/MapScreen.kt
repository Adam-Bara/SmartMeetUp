package com.example.smartmeetup.ui.map

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@Composable
fun MapScreen(
    uiState: MapUiState,
    onCreateEventClick: () -> Unit,
    onLocationPermissionResult: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onLocationPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onLocationPermissionResult(true)
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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

                    controller.setCenter(
                        GeoPoint(52.5200, 13.4050)
                    )
                }
            },
            update = { mapView ->
                mapView.overlays.clear()

                val userLatitude = uiState.userLatitude
                val userLongitude = uiState.userLongitude

                if (userLatitude != null && userLongitude != null) {
                    val userLocation = GeoPoint(
                        userLatitude,
                        userLongitude
                    )

                    mapView.controller.setCenter(userLocation)

                    val radiusCircle = Polygon().apply {
                        points = Polygon.pointsAsCircle(
                            userLocation,
                            uiState.radiusInMeters
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
                        position = userLocation
                        title = "Dein Standort"
                        setAnchor(
                            Marker.ANCHOR_CENTER,
                            Marker.ANCHOR_BOTTOM
                        )
                    }

                    mapView.overlays.add(userMarker)

                    uiState.nearbyEvents.forEach { event ->
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