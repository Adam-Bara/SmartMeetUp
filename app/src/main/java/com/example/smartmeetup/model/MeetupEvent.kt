package com.example.smartmeetup.model

data class MeetupEvent(
    val id: Int,
    val title: String,
    val category: String,
    val previewText: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val locationName: String,
    val host: User,
    val participants: List<User>,
    val participantLimit: Int,
    val status: EventStatus,
    val imageType: EventImageType = EventImageType.Park,
    val imagePlaceholderCount: Int = 5
) {
    val participantCount: Int
        get() = participants.size

    val participantStatus: String
        get() = "$participantCount/$participantLimit"

    val timeRange: String
        get() = "$startTime Uhr bis $endTime Uhr"
}

enum class EventStatus(
    val label: String
) {
    Ongoing("Laufend!"),
    StartingSoon("Startet bald"),
    StartingTomorrow("Startet morgen"),
    Upcoming("Upcoming"),
    Archived("Archived"),
    Cancelled("Cancelled")
}

enum class EventImageType {
    Park,
    Tennis,
    Picnic,
    Workshop,
    Hiking,
    Birthday
}