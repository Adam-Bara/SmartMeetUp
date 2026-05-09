package com.example.smartmeetup.model

data class MeetupEvent(
    val id: Int,
    val title: String,
    val category: String,
    val previewText: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val locationName: String,
    val host: User,
    val participants: List<User>,
    val participantLimit: Int,
    val imagePlaceholderCount: Int = 5
) {
    val participantCount: Int
        get() = participants.size

    val participantStatus: String
        get() = "$participantCount / $participantLimit"

    val timeRange: String
        get() = "$startTime Uhr bis $endTime Uhr"
}