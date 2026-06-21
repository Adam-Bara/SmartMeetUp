// File Purpose: UI screen that displays a grid of events, categorized by status (e.g., Live, Upcoming).
// Communication: EventViewModel, MeetupEvent, EventCard, EventPreviewScreen.
// Owner: Daria Zecha

package com.example.smartmeetup.ui.events.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.EventStatus
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.ui.events.assets.eventImageRes
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.R
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

private enum class EventListFilter {
    All,
    Upcoming,
    Past
}

@Composable
fun EventListScreen(
    events: List<MeetupEvent>,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onEventClick: (MeetupEvent) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf(EventListFilter.All) }

    val upcomingEvents = events.filter {
        it.status != EventStatus.Archived && it.status != EventStatus.Cancelled
    }

    val pastEvents = events.filter {
        it.status == EventStatus.Archived || it.status == EventStatus.Cancelled
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            EventListHeader(
                onCloseClick = onCloseClick,
                modifier = Modifier.padding(top = 22.dp)
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            EventFilterTabs(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
        }

        if (selectedFilter == EventListFilter.All || selectedFilter == EventListFilter.Upcoming) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EventSectionTitle(
                    text = "Upcoming",
                    color = Color(0xFF20A347),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            items(upcomingEvents) { event ->
                EventOverviewCard(
                    event = event,
                    isPast = false,
                    onClick = { onEventClick(event) }
                )
            }
        }

        if (selectedFilter == EventListFilter.All || selectedFilter == EventListFilter.Past) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EventSectionTitle(
                    text = "Past Events",
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 26.dp)
                )
            }

            items(pastEvents) { event ->
                EventOverviewCard(
                    event = event,
                    isPast = true,
                    onClick = { onEventClick(event) }
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
@Composable
private fun EventListHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF111827),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            LandscapeAvatar()
        }

        Text(
            text = "My Events",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF111827),
            modifier = Modifier.padding(top = 10.dp)
        )

        Text(
            text = "Your upcoming and past events",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun LandscapeAvatar() {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFFCCE7FD))
    ) {
        Image(
            painter = painterResource(id = R.drawable.event_image_park),
            contentDescription = "Profile",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFFF6C91D))
        )
    }
}

@Composable
private fun EventFilterTabs(
    selectedFilter: EventListFilter,
    onFilterSelected: (EventListFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        EventFilterTab(
            text = "All Events",
            selected = selectedFilter == EventListFilter.All,
            modifier = Modifier.weight(1f),
            onClick = { onFilterSelected(EventListFilter.All) }
        )

        EventFilterTab(
            text = "Upcoming",
            selected = selectedFilter == EventListFilter.Upcoming,
            modifier = Modifier.weight(1f),
            onClick = { onFilterSelected(EventListFilter.Upcoming) }
        )

        EventFilterTab(
            text = "Past",
            selected = selectedFilter == EventListFilter.Past,
            modifier = Modifier.weight(1f),
            onClick = { onFilterSelected(EventListFilter.Past) }
        )
    }
}

@Composable
private fun EventFilterTab(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (selected) Color(0xFFEAF5FF)
                else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color(0xFF0368F6) else Color(0xFF6B7280)
        )
    }
}

@Composable
private fun EventSectionTitle(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 15.sp,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        color = color,
        modifier = modifier
    )
}


@Composable
private fun EventOverviewCard(
    event: MeetupEvent,
    isPast: Boolean,
    onClick: () -> Unit
) {
    val statusText = eventListStatusLabel(event.status)
    val statusColor = eventListStatusColor(event.status)
    val grayscaleMatrix = ColorMatrix().apply {
        setToSaturation(0f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F3F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(108.dp)
            ) {
                Image(
                    painter = painterResource(id = eventImageRes(event.imageType)),
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isPast) 0.55f else 1f),
                    contentScale = ContentScale.Crop,
                    colorFilter = if (isPast) ColorFilter.colorMatrix(grayscaleMatrix) else null
                )

                CalendarBubble(
                    color = if (isPast) Color(0xFF6B7280) else statusColor,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 6.dp, y = 12.dp)
                )

                StatusPill(
                    text = statusText,
                    color = statusColor,
                    muted = isPast,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-6).dp, y = 12.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = event.date,
                    fontSize = 9.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1
                )

                Text(
                    text = event.title,
                    fontSize = 13.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = event.previewText,
                    fontSize = 9.sp,
                    lineHeight = 12.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Status: ${event.status.label}",
                    fontSize = 9.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ParticipantAvatarStack(
                        event = event,
                        muted = isPast
                    )

                    Spacer(modifier = Modifier.weight(1f))


                }
            }
        }
    }
}

@Composable
private fun CalendarBubble(
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(28.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    color: Color,
    muted: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (muted) Color(0xFFF3F4F6)
                else Color(0xFFE8F7E9)
            )
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = text,
            fontSize = 8.sp,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color,
            maxLines = 1,
        )
    }
}


@Composable
private fun ParticipantAvatarStack(
    event: MeetupEvent,
    muted: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        event.participants.take(3).forEachIndexed { index, user ->
            Box(
                modifier = Modifier
                    .offset(x = (-6 * index).dp)
                    .size(21.dp)
                    .clip(CircleShape)
                    .background(avatarColor(index, muted))
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.take(1),
                    fontSize = 9.sp,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        val remaining = event.participants.size - 3

        if (remaining > 0) {
            Box(
                modifier = Modifier
                    .offset(x = (-14).dp)
                    .size(23.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$remaining",
                    fontSize = 8.sp,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    maxLines = 1
                )
            }
        }
    }
}


private fun eventListStatusLabel(status: EventStatus): String {
    return when (status) {
        EventStatus.Archived -> "Archived"
        EventStatus.Cancelled -> "Cancelled"
        else -> "Upcoming"
    }
}

private fun eventListStatusColor(status: EventStatus): Color {
    return when (status) {
        EventStatus.Archived -> Color(0xFF0368F6)
        EventStatus.Cancelled -> Color(0xFFFF3B30)
        else -> Color(0xFF5BB854)
    }
}

private fun avatarColor(index: Int, muted: Boolean): Color {
    if (muted) {
        return when (index % 3) {
            0 -> Color(0xFF6B7280)
            1 -> Color(0xFF9CA3AF)
            else -> Color(0xFFD1D5DB)
        }
    }

    return when (index % 3) {
        0 -> Color(0xFF0368F6)
        1 -> Color(0xFF5BB854)
        else -> Color(0xFFF6C91D)
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun EventListScreenPreview() {
    SmartMeetUpTheme {
        EventListScreen(events = dummyEvents)
    }
}