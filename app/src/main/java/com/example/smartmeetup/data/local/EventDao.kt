package com.example.smartmeetup.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY id DESC")
    fun observeAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events ORDER BY id DESC")
    suspend fun getAllEventsOnce(): List<EventEntity>

    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    suspend fun getEventById(eventId: Int): EventEntity?

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM events WHERE id = :eventId)")
    suspend fun eventIdExists(eventId: Int): Boolean

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM events
            WHERE title = :title
            AND date = :date
            AND startTime = :startTime
            AND locationName = :locationName
        )
        """
    )
    suspend fun logicalDuplicateExists(
        title: String,
        date: String,
        startTime: String,
        locationName: String
    ): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Int)
}
