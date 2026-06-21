// File Purpose: Data model for a User, representing both hosts and event participants.
// Communication: MeetupEvent, ChatMessage, ParticipantList, and ProfileScreen.
// Owner: Daria Zecha

package com.example.smartmeetup.model

data class User(
    val id: Int,
    val displayName: String,
    val username: String? = null
)