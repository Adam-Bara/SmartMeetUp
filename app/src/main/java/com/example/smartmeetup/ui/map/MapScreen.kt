// File Purpose: UI for displaying the map using OSMDroid, showing nearby events, and handling location permissions.
// Communication: MapViewModel, MapUiState, MeetupEvent, EventPreviewScreen.
// Owner: Rina

package com.example.smartmeetup.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.ui.events.screens.EventPreviewScreen
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@Composable
fun MapScreen(
    uiState: MapUiState,
    selectedPreviewEvent: MeetupEvent?,
    onEventPinClick: (MeetupEvent) -> Unit,
    onDismissEventPreview: () -> Unit,
    onShowEventDetailsClick: (MeetupEvent) -> Unit,
    onCreateEventClick: () -> Unit,
    onLocationPermissionResult: (Boolean) -> Unit,
    onRefreshLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // GEÄNDERT:
    // Wir merken uns die MapView, damit die eigenen Compose-Zoombuttons
    // später auf mapView.controller zugreifen können.
    var mapViewForZoomControls by remember {
        mutableStateOf<MapView?>(null)
    }

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

                    // Bestehende Gesten bleiben aktiv:
                    // Pinch-to-zoom und Verschieben funktionieren weiterhin.
                    setMultiTouchControls(true)

                    // GEÄNDERT:
                    // Die Standard-Zoom-Buttons von osmdroid werden ausgeblendet.
                    // Dadurch liegen sie nicht mehr hinter Event Filter / Event erstellen.
                    zoomController.setVisibility(
                        CustomZoomButtonsController.Visibility.NEVER
                    )

                    controller.setZoom(15.0)

                    controller.setCenter(
                        GeoPoint(52.5200, 13.4050)
                    )

                    mapViewForZoomControls = this
                }
            },
            update = { mapView ->
                // GEÄNDERT:
                // Sicherheitshalber wird die aktuelle MapView auch im Update gesetzt.
                mapViewForZoomControls = mapView

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
                            setOnMarkerClickListener { _, _ ->
                                onEventPinClick(event)
                                true
                            }
                        }

                        mapView.overlays.add(eventMarker)
                    }
                }

                mapView.invalidate()
            }
        )

        MapStatusCard(
            isLoadingLocation = uiState.isLoadingLocation,
            locationErrorMessage = uiState.locationErrorMessage,
            hasUserLocation = uiState.userLatitude != null && uiState.userLongitude != null,
            onRefreshLocationClick = onRefreshLocationClick,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        // NEU:
        // Eigene Zoom-Buttons als Compose Overlay.
        // Sie liegen rechts mittig und nicht mehr unten hinter den Action-Buttons.
        MapZoomControls(
            onZoomInClick = {
                mapViewForZoomControls?.let { mapView ->
                    mapView.controller.setZoom(
                        mapView.zoomLevelDouble + 1.0
                    )
                    mapView.invalidate()
                }
            },
            onZoomOutClick = {
                mapViewForZoomControls?.let { mapView ->
                    mapView.controller.setZoom(
                        mapView.zoomLevelDouble - 1.0
                    )
                    mapView.invalidate()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )

        MapActionButtons(
            onCreateEventClick = onCreateEventClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        )

        selectedPreviewEvent?.let { event ->
            EventPreviewOverlay(
                event = event,
                onDismiss = onDismissEventPreview,
                onShowDetailsClick = {
                    onShowEventDetailsClick(event)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun EventPreviewOverlay(
    event: MeetupEvent,
    onDismiss: () -> Unit,
    onShowDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        ) {
            EventPreviewScreen(
                event = event,
                onCloseClick = onDismiss,
                onDetailsClick = onShowDetailsClick
            )
        }
    }
}

// NEU:
// Eigene Zoom-Controls.
// Vorteil: Die Position ist vollständig durch Compose steuerbar.
@Composable
private fun MapZoomControls(
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ZoomButton(
            text = "+",
            onClick = onZoomInClick
        )

        ZoomButton(
            text = "−",
            onClick = onZoomOutClick
        )
    }
}

// NEU:
// Kleiner runder Button für + und -.
// Ist bewusst separat, damit der Code wiederverwendbar und übersichtlich bleibt.
@Composable
private fun ZoomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(48.dp),
        onClick = onClick,
        shape = CircleShape,
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0368F6)
            )
        }
    }
}

// Diese Row platziert den Filter-Button links neben dem Create-Button.
@Composable
private fun MapActionButtons(
    onCreateEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EventFilterButton(
            onClick = {
                // TODO: Filter-Funktion später ergänzen
            }
        )

        CreateEventButton(
            onClick = onCreateEventClick
        )
    }
}

@Composable
private fun EventFilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(160.dp)
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
                text = "Event Filter",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
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

@Composable
private fun MapStatusCard(
    isLoadingLocation: Boolean,
    locationErrorMessage: String?,
    hasUserLocation: Boolean,
    onRefreshLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusText = when {
        isLoadingLocation -> "Standort wird geladen …"
        locationErrorMessage != null -> locationErrorMessage
        hasUserLocation -> "Dein Standort ist aktiv"
        else -> "Standort noch nicht verfügbar"
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Button(
                onClick = onRefreshLocationClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0368F6),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Standort aktualisieren")
            }
        }
    }
}