package com.example.smartmeetup.data.local

import com.example.smartmeetup.model.MeetupEvent

fun MeetupEvent.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        title = title,
        category = category,
        previewText = previewText,
        description = description,
        date = date,
        startTime = startTime,
        endTime = endTime,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        host = host,
        participants = participants,
        participantLimit = participantLimit,
        status = status,
        imageType = imageType,
        imagePlaceholderCount = imagePlaceholderCount
    )
}

fun EventEntity.toMeetupEvent(): MeetupEvent {
    return MeetupEvent(
        id = id,
        title = title,
        category = category,
        previewText = previewText,
        description = description,
        date = date,
        startTime = startTime,
        endTime = endTime,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        host = host,
        participants = participants,
        participantLimit = participantLimit,
        status = status,
        imageType = imageType,
        imagePlaceholderCount = imagePlaceholderCount
    )
}
