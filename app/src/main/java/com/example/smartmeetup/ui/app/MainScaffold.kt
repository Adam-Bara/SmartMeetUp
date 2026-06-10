package com.example.smartmeetup.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.ui.create.CreateEventScreen
import com.example.smartmeetup.ui.events.screens.EventListScreen
import com.example.smartmeetup.ui.map.MapScreen
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.viewmodel.MapViewModel
import com.example.smartmeetup.ui.chat.AllMessagesScreen

// Enum für die verschiedenen Hauptbereiche der App.
// Jeder Tab besitzt einen Titel, der später z. B. für Labels genutzt werden kann.
private enum class MainTab(
    val title: String
) {
    Events("Events"),
    MyEvents("My Events"),
    Messages("Messages"),
    Profile("Profile")
}

// Aktiviert experimentelle Material-3-APIs, da TopAppBar diese Annotation benötigt.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier
) {
    // Speichert den aktuell ausgewählten Tab.
    // remember sorgt dafür, dass der Zustand bei Recomposition erhalten bleibt.
    var selectedTab by remember { mutableStateOf(MainTab.Events) }
    var showCreateEventScreen by remember { mutableStateOf(false) }

    val mapViewModel: MapViewModel = viewModel()
    val mapUiState by mapViewModel.uiState.collectAsState()

    // Scaffold stellt die Grundstruktur der App bereit:
    // TopAppBar, Bottom Navigation und Inhaltsbereich.
    Scaffold(
        modifier = modifier.fillMaxSize(),

        // Obere App-Leiste mit dem App-Namen
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Smart Meetup")
                }
            )
        },

        // Untere Navigation zu den Hauptbereichen der App
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(96.dp),
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                //Events Map
                NavigationBarItem(
                    selected = selectedTab == MainTab.Events,
                    onClick = { selectedTab = MainTab.Events },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Events",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text("Events")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF007AFF),
                        selectedTextColor = Color(0xFF007AFF),
                        unselectedIconColor = Color(0xFF8E8E93),
                        unselectedTextColor = Color(0xFF8E8E93),
                        indicatorColor = Color.Transparent
                    )
                )

                //Gespeicherte Events
                NavigationBarItem(
                    selected = selectedTab == MainTab.MyEvents,
                    onClick = { selectedTab = MainTab.MyEvents },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "My Events",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text("My Events")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF007AFF),
                        selectedTextColor = Color(0xFF007AFF),
                        unselectedIconColor = Color(0xFF8E8E93),
                        unselectedTextColor = Color(0xFF8E8E93),
                        indicatorColor = Color.Transparent
                    )
                )

                //Messages
                NavigationBarItem(
                    selected = selectedTab == MainTab.Messages,
                    onClick = { selectedTab = MainTab.Messages },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = "Messages",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text("Messages")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF007AFF),
                        selectedTextColor = Color(0xFF007AFF),
                        unselectedIconColor = Color(0xFF8E8E93),
                        unselectedTextColor = Color(0xFF8E8E93),
                        indicatorColor = Color.Transparent
                    )
                )

                //Profile
                NavigationBarItem(
                    selected = selectedTab == MainTab.Profile,
                    onClick = { selectedTab = MainTab.Profile },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text("Profile")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF007AFF),
                        selectedTextColor = Color(0xFF007AFF),
                        unselectedIconColor = Color(0xFF8E8E93),
                        unselectedTextColor = Color(0xFF8E8E93),
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }

    ) { innerPadding ->

        // Box dient als Container für den eigentlichen Inhalt.
        // Das Padding verhindert, dass Inhalte unter TopAppBar oder NavigationBar liegen.
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            // Je nach ausgewähltem Tab wird ein anderer Screen angezeigt.
            when (selectedTab) {

                MainTab.Events -> {
                    if (showCreateEventScreen) {
                        CreateEventScreen(
                            onCloseClick = { showCreateEventScreen = false },
                            onPublishClick = { showCreateEventScreen = false }
                        )
                    } else {
                        MapScreen(
                            uiState = mapUiState,
                            onCreateEventClick = { showCreateEventScreen = true },
                            onLocationPermissionResult = mapViewModel::onLocationPermissionResult
                        )
                    }
                }

                MainTab.MyEvents -> {
                    EventListScreen(events = dummyEvents)
                }

                MainTab.Messages -> {
                    PlaceholderScreen(
                        title = "Messages",
                        text = "Here users will see a list of all messages."
                    )
                }

                MainTab.Profile -> {
                    PlaceholderScreen(
                        title = "Profile",
                        text = "Here users will see their profile and settings."
                    )
                }
            }
        }
    }
}

// Preview-Funktion für Android Studio.
// Damit kann der MainScaffold direkt in der Vorschau angezeigt werden.
@Preview(showBackground = true)
@Composable
fun MainScaffoldPreview() {
    SmartMeetUpTheme {
        MainScaffold()
    }
}