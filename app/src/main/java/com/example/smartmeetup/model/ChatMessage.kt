package com.example.smartmeetup.model

data class ChatMessage(
    val id: Int,
    val eventId: Int,
    val sender: User,
    val text: String,
    val time: String,
    val isOwnMessage: Boolean
)