package com.example.smartmeetup.ui.events.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.R
import com.example.smartmeetup.data.dummy.selectedDummyEvent
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import androidx.compose.foundation.background

enum class EventJoinedStatus {
    NOT_STARTED,
    ONGOING
}

@Composable
fun EventJoinedScreen(
    event: MeetupEvent = selectedDummyEvent,
    status: EventJoinedStatus = EventJoinedStatus.NOT_STARTED,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLeaveEventClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onParticipantsClick: () -> Unit = {}
) {
    val state = eventJoinedUiState(status)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = eventJoinedBackgroundRes(status)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 25.dp, bottom = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EventJoinedTopBar(
                onBackClick = onBackClick,
                onLeaveEventClick = onLeaveEventClick
            )

            Spacer(modifier = Modifier.height(25.dp))

            EventJoinedTitle(
                event = event,
                state = state
            )

            Spacer(modifier = Modifier.height(15.dp))

            EventJoinedTimer(
                state = state
            )

            Spacer(modifier = Modifier.height(30.dp))

            EventJoinedInfoCard(event = event)

            Spacer(modifier = Modifier.height(20.dp))

            EventJoinedChatRow(onClick = onChatClick)

            Spacer(modifier = Modifier.height(10.dp))

            EventJoinedParticipantsRow(
                event = event,
                onClick = onParticipantsClick
            )
        }
    }
}

@Composable
private fun EventJoinedTopBar(
    onBackClick: () -> Unit,
    onLeaveEventClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier
                .size(46.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBackClick
                ),
            shape = RoundedCornerShape(15.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = Color(0xFF111827),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Button(
            onClick = onLeaveEventClick,
            modifier = Modifier
                .height(42.dp)
                .width(132.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF4B4B),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Leave event",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EventJoinedTitle(
    event: MeetupEvent,
    state: EventJoinedUiState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Event ${event.id}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B1220)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "| ${event.category}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF0B1220)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(state.statusDotColor, CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = state.statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1F2933)
            )
        }
    }
}

@Composable
private fun EventJoinedTimer(
    state: EventJoinedUiState
) {
    Box(
        modifier = Modifier.size(148.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.minDimension * 0.40f
            val middleRadius = size.minDimension * 0.40f
            val ringSize = size.minDimension * 0.040f

            drawCircle(
                color = state.accentColor.copy(alpha = 0.08f),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 1.5f)
            )

            drawCircle(
                color = state.accentColor.copy(alpha = 0.12f),
                radius = outerRadius * 0.82f,
                center = center,
                style = Stroke(width = 1.5f)
            )

            drawArc(
                color = state.accentColor.copy(alpha = 0.13f),
                startAngle = -90f,
                sweepAngle = 300f,
                useCenter = false,
                style = Stroke(width = ringSize, cap = StrokeCap.Round)
            )

            drawArc(
                color = state.accentColor,
                startAngle = -90f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = ringSize, cap = StrokeCap.Round)
            )

            drawCircle(
                color = Color.White,
                radius = middleRadius,
                center = center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "24:37",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF020617)
            )

            Text(
                text = state.timerLabel,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
private fun EventJoinedInfoCard(
    event: MeetupEvent
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.93f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            EventJoinedTextSection(
                title = "ABOUT",
                body = "A relaxed football meet up" //manually adjusted as Dummy text info too long
            )

            EventJoinedDivider()

            EventJoinedIconSection(
                title = "WHEN",
                icon = Icons.Outlined.AccessTime,
                body = "2PM to 4PM  |  24.05.26" //manually adjusted as Dummy data text too long
            )

            EventJoinedDivider()

            EventJoinedIconSection(
                title = "WHERE",
                icon = Icons.Outlined.LocationOn,
                body = event.locationName
            )

            EventJoinedDivider()

            EventJoinedHostSection(event = event)
        }
    }
}

@Composable
private fun EventJoinedTextSection(
    title: String,
    body: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        EventJoinedSectionTitle(title = title)

        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF4B5563)
        )
    }
}

@Composable
private fun EventJoinedIconSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    body: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        EventJoinedSectionTitle(title = title)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EventJoinedHostSection(
    event: MeetupEvent
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        EventJoinedSectionTitle(title = "HOSTED BY")

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_image_mountain),
                contentDescription = event.host.displayName,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = event.host.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }
    }
}

@Composable
private fun EventJoinedSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF5BB854)
    )
}

@Composable
private fun EventJoinedDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color(0xFFE5E7EB)
    )
}

@Composable
private fun EventJoinedChatRow(
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFDCEEFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubble,
                    contentDescription = "Live Chat",
                    tint = Color(0xFF0368F6),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Live Chat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = "Message all participants",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    maxLines = 1
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFDCEEFF)
            ) {
                Text(
                    text = "3 new",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0368F6)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = "Open Chat",
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun EventJoinedParticipantsRow(
    event: MeetupEvent,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFFEAF7EA).copy(alpha = 0.95f),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Participants",
                tint = Color(0xFF5BB854),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Participants ${event.participantStatus}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5BB854),
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            EventJoinedAvatarStack()

            val remainingParticipants = (event.participantCount - 3).coerceAtLeast(0)

            if (remainingParticipants > 0) {
                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    modifier = Modifier.size(30.dp),
                    shape = CircleShape,
                    color = Color(0xFFDDF2DD)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "+$remainingParticipants",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5BB854)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventJoinedAvatarStack() {
    val avatars = listOf(
        R.drawable.profile_image_mountain,
        R.drawable.profile_image_flower,
        R.drawable.profile_image_wave
    )

    Box(
        modifier = Modifier.width(82.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        avatars.forEachIndexed { index, avatar ->
            Image(
                painter = painterResource(id = avatar),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = (index * 22).dp)
                    .size(35.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}



private data class EventJoinedUiState(
    val statusText: String,
    val timerLabel: String,
    val accentColor: Color,
    val statusDotColor: Color
)

private fun eventJoinedUiState(status: EventJoinedStatus): EventJoinedUiState {
    return when (status) {
        EventJoinedStatus.NOT_STARTED -> EventJoinedUiState(
            statusText = "Not started yet",
            timerLabel = "Starts in",
            accentColor = Color(0xFF0368F6),
            statusDotColor = Color(0xFFFF315B)
        )

        EventJoinedStatus.ONGOING -> EventJoinedUiState(
            statusText = "Live now",
            timerLabel = "Time remaining",
            accentColor = Color(0xFF5BB854),
            statusDotColor = Color(0xFF5BB854)
        )
    }
}


@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun EventJoinedScreenNotStartedPreview() {
    SmartMeetUpTheme {
        EventJoinedScreen(
            status = EventJoinedStatus.NOT_STARTED
        )
    }
}

@DrawableRes
private fun eventJoinedBackgroundRes(status: EventJoinedStatus): Int {
    return when (status) {
        EventJoinedStatus.NOT_STARTED -> R.drawable.event_joined_not_started
        EventJoinedStatus.ONGOING -> R.drawable.event_joined_running
    }
}
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun EventJoinedScreenOngoingPreview() {
    SmartMeetUpTheme {
        EventJoinedScreen(
            status = EventJoinedStatus.ONGOING
        )
    }
}