// File Purpose: The primary scaffold of the app, managing bottom navigation and top app bar.
// Communication: EventViewModel, MapViewModel, and all main tab screens (Events, Map, Messages, Profile).
// Owner: Rina

package com.example.smartmeetup.ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.smartmeetup.ui.chat.AllMessagesScreen
import com.example.smartmeetup.ui.chat.ChatScreen
import com.example.smartmeetup.ui.create.CreateEventScreen
import com.example.smartmeetup.ui.events.components.EventCard
import com.example.smartmeetup.ui.events.components.ParticipantList
import com.example.smartmeetup.ui.events.screens.EventJoinedScreen
import com.example.smartmeetup.ui.events.screens.EventJoinedStatus
import com.example.smartmeetup.ui.events.screens.EventListScreen
import com.example.smartmeetup.ui.map.MapScreen
import com.example.smartmeetup.ui.profile.ProfileScreen
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.viewmodel.EventViewModel
import com.example.smartmeetup.viewmodel.MapViewModel

private const val MY_EVENTS_TITLE = "My Events"
private enum class MainTab(
    val title: String
) {
    Events("Events"),
    MyEvents(MY_EVENTS_TITLE),
    Messages("Messages"),
    Profile("Profile")
}

private enum class EventsScreen {
    Map,
    CreateEvent,
    EventDetails
}

private enum class MyEventsScreen {
    EventList,
    EventJoined,
    Participants,
    Chat
}

private enum class MessagesScreen {
    Overview,
    Chat
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(MainTab.Events) }
    var eventsScreen by remember { mutableStateOf(EventsScreen.Map) }
    var selectedPreviewEventId by remember { mutableStateOf<Int?>(null) }
    var myEventsScreen by remember { mutableStateOf(MyEventsScreen.EventList) }
    var messagesScreen by remember { mutableStateOf(MessagesScreen.Overview) }

    val mapViewModel: MapViewModel = viewModel()
    val mapUiState by mapViewModel.uiState.collectAsState()

    val eventViewModel: EventViewModel = viewModel()
    val joinedEvents by eventViewModel.joinedEvents.collectAsState()
    val selectedEvent by eventViewModel.selectedEvent.collectAsState()

    val selectedPreviewEvent = mapUiState.nearbyEvents.firstOrNull { event ->
        event.id == selectedPreviewEventId
    }

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
            NavigationBar(
                modifier = Modifier.height(96.dp),
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == MainTab.Events,
                    onClick = {
                        selectedTab = MainTab.Events
                        eventsScreen = EventsScreen.Map
                        selectedPreviewEventId = null
                    },
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
                    colors = smartMeetupNavigationItemColors()
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.MyEvents,
                    onClick = {
                        selectedTab = MainTab.MyEvents
                        eventsScreen = EventsScreen.Map
                        selectedPreviewEventId = null
                        myEventsScreen = MyEventsScreen.EventList
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = MY_EVENTS_TITLE,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = {
                        Text(MY_EVENTS_TITLE)
                    },
                    colors = smartMeetupNavigationItemColors()
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.Messages,
                    onClick = {
                        selectedTab = MainTab.Messages
                        eventsScreen = EventsScreen.Map
                        selectedPreviewEventId = null
                    },
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
                    colors = smartMeetupNavigationItemColors()
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.Profile,
                    onClick = {
                        selectedTab = MainTab.Profile
                        eventsScreen = EventsScreen.Map
                        selectedPreviewEventId = null
                    },
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
                    colors = smartMeetupNavigationItemColors()
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (selectedTab) {

                MainTab.Events -> {
                    when (eventsScreen) {
                        EventsScreen.Map -> {
                            MapScreen(
                                uiState = mapUiState,
                                selectedPreviewEvent = selectedPreviewEvent,
                                onEventPinClick = { event ->
                                    selectedPreviewEventId = event.id
                                },
                                onDismissEventPreview = {
                                    selectedPreviewEventId = null
                                },
                                onShowEventDetailsClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    selectedPreviewEventId = null
                                    eventsScreen = EventsScreen.EventDetails
                                },
                                onCreateEventClick = {
                                    selectedPreviewEventId = null
                                    eventsScreen = EventsScreen.CreateEvent
                                },
                                onLocationPermissionResult = mapViewModel::onLocationPermissionResult,
                                onRefreshLocationClick = mapViewModel::refreshUserLocation
                            )
                        }

                        EventsScreen.CreateEvent -> {
                            CreateEventScreen(
                                onCloseClick = {
                                    eventsScreen = EventsScreen.Map
                                },
                                onPublishClick = {
                                    eventsScreen = EventsScreen.Map
                                }
                            )
                        }

                        EventsScreen.EventDetails -> {
                            selectedEvent?.let { event ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    EventCard(
                                        event = event,
                                        onCloseClick = {
                                            eventViewModel.clearSelectedEvent()
                                            eventsScreen = EventsScreen.Map
                                        },
                                        onParticipantsClick = {
                                            selectedTab = MainTab.MyEvents
                                            myEventsScreen = MyEventsScreen.Participants
                                        },
                                        onJoinClick = {
                                            myEventsScreen = MyEventsScreen.EventJoined
                                            selectedTab = MainTab.MyEvents
                                        }
                                    )
                                }
                            } ?: MapScreen(
                                uiState = mapUiState,
                                selectedPreviewEvent = selectedPreviewEvent,
                                onEventPinClick = { event ->
                                    selectedPreviewEventId = event.id
                                },
                                onDismissEventPreview = {
                                    selectedPreviewEventId = null
                                },
                                onShowEventDetailsClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    selectedPreviewEventId = null
                                    eventsScreen = EventsScreen.EventDetails
                                },
                                onCreateEventClick = {
                                    selectedPreviewEventId = null
                                    eventsScreen = EventsScreen.CreateEvent
                                },
                                onLocationPermissionResult = mapViewModel::onLocationPermissionResult,
                                onRefreshLocationClick = mapViewModel::refreshUserLocation
                            )
                        }
                    }
                }

                MainTab.MyEvents -> {
                    when (myEventsScreen) {

                        MyEventsScreen.EventList -> {
                            EventListScreen(
                                events = joinedEvents,
                                onEventClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    myEventsScreen = MyEventsScreen.EventJoined
                                }
                            )
                        }

                        MyEventsScreen.EventJoined -> {
                            selectedEvent?.let { event ->
                                EventJoinedScreen(
                                    event = event,
                                    status = EventJoinedStatus.NOT_STARTED,
                                    onBackClick = {
                                        myEventsScreen = MyEventsScreen.EventList
                                    },
                                    onParticipantsClick = {
                                        myEventsScreen = MyEventsScreen.Participants
                                    },
                                    onChatClick = {
                                        myEventsScreen = MyEventsScreen.Chat
                                    },
                                    onLeaveEventClick = {
                                        eventViewModel.leaveEvent(event.id)
                                        myEventsScreen = MyEventsScreen.EventList
                                    }
                                )
                            } ?: EventListScreen(
                                events = joinedEvents,
                                onEventClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    myEventsScreen = MyEventsScreen.EventJoined
                                }
                            )
                        }

                        MyEventsScreen.Participants -> {
                            selectedEvent?.let { event ->
                                ParticipantList(
                                    participants = event.participants,
                                    participantStatus = event.participantStatus,
                                    onBackClick = {
                                        myEventsScreen = MyEventsScreen.EventJoined
                                    }
                                )
                            } ?: EventListScreen(
                                events = joinedEvents,
                                onEventClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    myEventsScreen = MyEventsScreen.EventJoined
                                }
                            )
                        }

                        MyEventsScreen.Chat -> {
                            selectedEvent?.let { event ->
                                ChatScreen(
                                    chatTitle = event.title,
                                    onBackClick = {
                                        myEventsScreen = MyEventsScreen.EventJoined
                                    }
                                )
                            } ?: EventListScreen(
                                events = joinedEvents,
                                onEventClick = { event ->
                                    eventViewModel.selectEvent(event.id)
                                    myEventsScreen = MyEventsScreen.EventJoined
                                }
                            )
                        }
                    }
                }

                MainTab.Messages -> {
                    when (messagesScreen) {
                        MessagesScreen.Overview -> {
                            AllMessagesScreen(
                                onMessageClick = {
                                    messagesScreen = MessagesScreen.Chat
                                }
                            )
                        }

                        MessagesScreen.Chat -> {
                            ChatScreen(
                                chatTitle = "Messages",
                                onBackClick = {
                                    messagesScreen = MessagesScreen.Overview
                                }
                            )
                        }
                    }
                }

                MainTab.Profile -> {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
private fun smartMeetupNavigationItemColors() =
    NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF007AFF),
        selectedTextColor = Color(0xFF007AFF),
        unselectedIconColor = Color(0xFF8E8E93),
        unselectedTextColor = Color(0xFF8E8E93),
        indicatorColor = Color.Transparent
    )

@Preview(showBackground = true)
@Composable
fun MainScaffoldPreview() {
    SmartMeetUpTheme {
        MainScaffold()
    }
}