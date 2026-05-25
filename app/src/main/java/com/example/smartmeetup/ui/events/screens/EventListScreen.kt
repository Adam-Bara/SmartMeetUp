package com.example.smartmeetup.ui.events.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.data.dummy.dummyEvents
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.model.EventStatus

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

    val upcomingEvents = events
    val pastEvents = events.take(3)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        item {
            EventListHeader(
                onCloseClick = onCloseClick,
                modifier = Modifier.padding(top = 22.dp)
            )
        }

        item {
            EventFilterTabs(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )
        }

        if (selectedFilter == EventListFilter.All || selectedFilter == EventListFilter.Upcoming) {
            item {
                EventSectionTitle(
                    text = "Upcoming",
                    color = Color(0xFF20A347)
                )
            }

            item {
                EventCardRow(
                    events = upcomingEvents,
                    isPastSection = false,
                    onEventClick = onEventClick
                )
            }
        }

        if (selectedFilter == EventListFilter.All || selectedFilter == EventListFilter.Past) {
            item {
                EventSectionTitle(
                    text = "Past Events",
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                EventCardRow(
                    events = pastEvents,
                    isPastSection = true,
                    onEventClick = onEventClick
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
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
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFCCE7FD))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(24.dp)
                .background(Color(0xFF5BB854))
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(7.dp)
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
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        color = color,
        modifier = modifier
    )
}
@Composable
private fun EventCardRow(
    events: List<MeetupEvent>,
    isPastSection: Boolean,
    onEventClick: (MeetupEvent) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(events) { index, event ->
            EventOverviewCard(
                event = event,
                index = index,
                isPast = isPastSection,
                onClick = { onEventClick(event) }
            )
        }
    }
}

@Composable
private fun EventOverviewCard(
    event: MeetupEvent,
    index: Int,
    isPast: Boolean,
    onClick: () -> Unit
) {
    val statusText = event.status.label
    val statusColor = eventStatusColor(event.status)
    val dateText = event.date

    Card(
        modifier = Modifier
            .width(156.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        border = BorderStroke(1.dp, Color(0xFFF1F3F5))
    ) {
        Column {
            Box {
                EventIllustration(
                    index = index,
                    muted = isPast,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                )

                CalendarBubble(
                    color = if (isPast) Color(0xFF6B7280) else statusColor,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 12.dp, y = 24.dp)
                )

                StatusPill(
                    text = statusText,
                    color = statusColor,
                    muted = isPast,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-10).dp, y = 22.dp)
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    top = 34.dp,
                    bottom = 14.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF6B7280)
                )

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = event.previewText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Status: $statusText",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ParticipantAvatarStack(
                        event = event,
                        muted = isPast
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ArrowButton(
                        muted = isPast,
                        onClick = onClick
                    )
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
        modifier = modifier.size(48.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(25.dp)
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
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (muted) Color(0xFFF3F4F6)
                else Color(0xFFE8F7E9)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun ArrowButton(
    muted: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(42.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open event details",
                tint = if (muted) Color(0xFF6B7280) else Color(0xFF0368F6),
                modifier = Modifier.size(26.dp)
            )
        }
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
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(avatarColor(index, muted))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.displayName.take(1),
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
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$remaining",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun EventIllustration(
    index: Int,
    muted: Boolean,
    modifier: Modifier = Modifier
) {
    val skyColor = if (muted) Color(0xFFE5E7EB) else Color(0xFFCCE7FD)
    val hillColor = if (muted) Color(0xFFD1D5DB) else Color(0xFFD0E8D1)
    val grassColor = if (muted) Color(0xFFBFC4CA) else Color(0xFF5BB854)
    val sunColor = if (muted) Color(0xFF9CA3AF) else Color(0xFFF6C91D)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        skyColor,
                        Color.White
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(22.dp)
                .clip(CircleShape)
                .background(sunColor)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 26.dp)
                .width(54.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.9f))
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(66.dp)
                .clip(RoundedCornerShape(topStart = 90.dp, topEnd = 90.dp))
                .background(hillColor)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(42.dp)
                .background(grassColor.copy(alpha = if (muted) 0.45f else 0.75f))
        )

        when (index % 3) {
            0 -> TreeScene(muted = muted)
            1 -> TennisScene(muted = muted)
            else -> PicnicScene(muted = muted)
        }
    }
}

@Composable
private fun TreeScene(muted: Boolean) {
    val treeColor = if (muted) Color(0xFF9CA3AF) else Color(0xFF20A347)
    val trunkColor = if (muted) Color(0xFF6B7280) else Color(0xFF255C3B)

    Box(
        modifier = Modifier
            .padding(start = 22.dp, top = 36.dp)
            .size(54.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(6.dp)
                .height(54.dp)
                .background(trunkColor)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(42.dp)
                .clip(CircleShape)
                .background(treeColor)
        )
    }

    Box(
        modifier = Modifier
            .padding(start = 92.dp, top = 54.dp)
            .size(38.dp)
            .clip(CircleShape)
            .background(treeColor.copy(alpha = 0.75f))
    )
}

@Composable
private fun TennisScene(muted: Boolean) {
    val blue = if (muted) Color(0xFF9CA3AF) else Color(0xFF0368F6)
    val ball = if (muted) Color(0xFF9CA3AF) else Color(0xFFF6C91D)

    Box(
        modifier = Modifier
            .alignCenterLike()
            .size(56.dp)
            .clip(CircleShape)
            .border(5.dp, blue, CircleShape)
    )

    Box(
        modifier = Modifier
            .padding(start = 64.dp, top = 86.dp)
            .width(12.dp)
            .height(48.dp)
            .background(blue)
    )

    Box(
        modifier = Modifier
            .padding(start = 42.dp, top = 82.dp)
            .size(24.dp)
            .clip(CircleShape)
            .background(ball)
    )
}

@Composable
private fun PicnicScene(muted: Boolean) {
    val basket = if (muted) Color(0xFF9CA3AF) else Color(0xFFF6C91D)
    val blanket = if (muted) Color(0xFFD1D5DB) else Color(0xFFFFE5E5)

    Box(
        modifier = Modifier
            .alignCenterLike()
            .width(76.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(blanket)
    )

    Box(
        modifier = Modifier
            .padding(start = 62.dp, top = 72.dp)
            .width(58.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(basket)
    )
}

private fun Modifier.alignCenterLike(): Modifier {
    return this
        .padding(start = 48.dp, top = 48.dp)
}


private fun eventStatusColor(status: EventStatus): Color {
    return when (status) {
        EventStatus.Cancelled -> Color(0xFFFF3B30)
        EventStatus.Archived -> Color(0xFF0368F6)
        EventStatus.Ongoing -> Color(0xFF20A347)
        EventStatus.StartingSoon -> Color(0xFF5BB854)
        EventStatus.StartingTomorrow -> Color(0xFF5BB854)
        EventStatus.Upcoming -> Color(0xFF5BB854)
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