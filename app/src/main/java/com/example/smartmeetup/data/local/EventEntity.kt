package com.example.smartmeetup.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.User

@Entity(
    tableName = "events",
    indices = [
        Index(
            value = ["title", "date", "startTime", "locationName"],
            unique = true
        )
    ]
)
data class EventEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val category: String,
    val previewText: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val host: User,
    val participants: List<User>,
    val participantLimit: Int,
    val status: EventStatus,
    val imageType: EventImageType,
    val imagePlaceholderCount: Int
)
