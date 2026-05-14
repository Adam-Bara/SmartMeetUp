package com.example.smartmeetup.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.ui.events.screens.EventListScreen
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

private enum class MainTab(
    val title: String
) {
    Events("Events"),
    Map("Map"),
    Create("Create"),
    Notifications("Notifications"),
    Profile("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(MainTab.Events) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Smart Meetup")
                }
            )
        },
        bottomBar = {
            NavigationBar {
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

                NavigationBarItem(
                    selected = selectedTab == MainTab.Map,
                    onClick = { selectedTab = MainTab.Map },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map"
                        )
                    },
                    label = {
                        Text("Map")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.Create,
                    onClick = { selectedTab = MainTab.Create },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Create event"
                        )
                    },
                    label = {
                        Text("Create")
                    }
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.Notifications,
                    onClick = { selectedTab = MainTab.Notifications },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    },
                    label = {
                        Text("Updates")
                    }
                )

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
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (selectedTab) {
                MainTab.Events -> {
                    EventListScreen(events = dummyEvents)
                }

                MainTab.Map -> {
                    PlaceholderScreen(
                        title = "Map",
                        text = "Here we will show nearby meetup events on a map."
                    )
                }

                MainTab.Create -> {
                    PlaceholderScreen(
                        title = "Create event",
                        text = "Here users will create a new spontaneous meetup."
                    )
                }

                MainTab.Notifications -> {
                    PlaceholderScreen(
                        title = "Notifications",
                        text = "Here users will see event updates and reminders."
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

@Preview(showBackground = true)
@Composable
fun MainScaffoldPreview() {
    SmartMeetUpTheme {
        MainScaffold()
    }
}