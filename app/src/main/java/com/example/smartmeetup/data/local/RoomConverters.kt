package com.example.smartmeetup.data.local

import androidx.room.TypeConverter
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.User

class RoomConverters {

    @TypeConverter
    fun fromEventStatus(status: EventStatus): String {
        return status.name
    }

    @TypeConverter
    fun toEventStatus(value: String): EventStatus {
        return EventStatus.valueOf(value)
    }

    @TypeConverter
    fun fromEventImageType(imageType: EventImageType): String {
        return imageType.name
    }

    @TypeConverter
    fun toEventImageType(value: String): EventImageType {
        return EventImageType.valueOf(value)
    }

    @TypeConverter
    fun fromUser(user: User): String {
        return listOf(
            user.id.toString(),
            user.displayName,
            user.username.orEmpty()
        ).joinToString(separator = USER_FIELD_SEPARATOR)
    }

    @TypeConverter
    fun toUser(value: String): User {
        val parts = value.split(USER_FIELD_SEPARATOR)

        return User(
            id = parts.getOrNull(0)?.toIntOrNull() ?: 0,
            displayName = parts.getOrNull(1).orEmpty(),
            username = parts.getOrNull(2)?.ifBlank { null }
        )
    }

    @TypeConverter
    fun fromUserList(users: List<User>): String {
        return users.joinToString(separator = USER_SEPARATOR) { user ->
            fromUser(user)
        }
    }

    @TypeConverter
    fun toUserList(value: String): List<User> {
        if (value.isBlank()) {
            return emptyList()
        }

        return value.split(USER_SEPARATOR).map { userString ->
            toUser(userString)
        }
    }

    private companion object {
        const val USER_SEPARATOR = ";;"
        const val USER_FIELD_SEPARATOR = "|"
    }
}
