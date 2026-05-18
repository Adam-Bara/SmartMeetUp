package com.example.smartmeetup.ui.app

// Layout-Imports für Container, Vollbildgröße und Padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// Icons für die Bottom Navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Forum

// Material-3-Komponenten
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

// Compose-State und Composable-Funktionen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

// Dummy-Daten und Screens der App
import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.ui.events.screens.EventListScreen
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

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

        // Untere Navigation mit den Hauptbereichen der App
        bottomBar = {
            NavigationBar {
                // Navigationseintrag für die Event-Liste
                NavigationBarItem(
                    selected = selectedTab == MainTab.Events,
                    onClick = { selectedTab = MainTab.Events },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Events"
                        )
                    },
                    label = {
                        Text("Events")
                    }
                )

                // Navigationseintrag für die Kartenansicht
                NavigationBarItem(
                    selected = selectedTab == MainTab.MyEvents,
                    onClick = { selectedTab = MainTab.MyEvents },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "My Events"
                        )
                    },
                    label = {
                        Text("My Events")
                    }
                )

                // Navigationseintrag zum Erstellen eines neuen Events
                NavigationBarItem(
                    selected = selectedTab == MainTab.Messages,
                    onClick = { selectedTab = MainTab.Messages },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Forum,
                            contentDescription = "Messages"
                        )
                    },
                    label = {
                        Text("Messages")
                    }
                )

                // Navigationseintrag für Profil und Einstellungen
                NavigationBarItem(
                    selected = selectedTab == MainTab.Profile,
                    onClick = { selectedTab = MainTab.Profile },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
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

                // Zeigt die Event-Liste mit Dummy-Daten an
                MainTab.Events -> {
                    EventListScreen(events = dummyEvents)
                }

                // Platzhalter für die spätere Kartenansicht
                MainTab.MyEvents -> {
                    PlaceholderScreen(
                        title = "Map",
                        text = "Here we will show nearby meetup events on a map."
                    )
                }

                // Platzhalter für den Screen zum Erstellen eines Events
                MainTab.Messages -> {
                    PlaceholderScreen(
                        title = "Create event",
                        text = "Here users will create a new spontaneous meetup."
                    )
                }

                // Platzhalter für Profil und Einstellungen
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