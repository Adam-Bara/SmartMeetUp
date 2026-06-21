// File Purpose: Main card component for displaying event details, host info, and participant count.
// Communication: EventListScreen, MeetupEvent, EventViewModel, ParticipantList.
// Owner: Daria Zecha

package com.example.smartmeetup.ui.events.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.data.dummy.selectedDummyEvent
import com.example.smartmeetup.model.MeetupEvent
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.ui.events.assets.eventImageRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.smartmeetup.R
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.remember
// Main card component for displaying detailed information about a MeetupEvent.
// Used as a central UI element in the events discovery flow to show event
// summaries, schedules, and host details.
@Composable
fun EventCard(
    event: MeetupEvent,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onParticipantsClick: () -> Unit = {},
    onJoinClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            CloseButton(onCloseClick = onCloseClick)

            EventHeroImage(event = event)

            EventTitleRow(event = event)

            EventDetailSection(
                title = "ABOUT",
                body = event.description
            )

            EventIconDetailSection(
                title = "WHEN",
                icon = Icons.Outlined.AccessTime,
                body = "${event.timeRange}  |  ${event.date}"
            )

            EventIconDetailSection(
                title = "WHERE",
                icon = Icons.Outlined.LocationOn,
                body = event.locationName
            )

            HostedBySection(event = event)

            Spacer(modifier = Modifier.height(22.dp))

            ParticipantsButton(
                participantStatus = event.participantStatus,
                onClick = onParticipantsClick
            )

            JoinEventButton(onClick = onJoinClick)
        }
    }
}

// Displays the primary image for the event.
// Uses eventImageRes() to map EventImageType (e.g., Park, Tennis) 
// to the correct drawable resource in the assets package.
@Composable
private fun EventHeroImage(
    event: MeetupEvent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(205.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = eventImageRes(EventImageType.Park)),
            contentDescription = event.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun CloseButton(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .offset(x = (-4).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCloseClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = "Close",
            tint = Color(0xFF1F2933),
            modifier = Modifier.size(28.dp)
        )
    }
}

// Displays the event's category and a secondary label.
// Dynamically renders information based on the MeetupEvent model's category field.
@Composable
private fun EventTitleRow(
    event: MeetupEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "Event ${event.id}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2933)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "| ${event.category}",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF1F2933),
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

// Reusable layout for simple text-based event details.
// Typically used for the "ABOUT" section to display the event description.
@Composable
private fun EventDetailSection(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5BB854)
        )

        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF4B5563)
        )
    }
}

// Reusable layout for details requiring an icon (e.g., Time or Location).
// Helps maintain visual consistency across the event's "WHEN" and "WHERE" sections.
@Composable
private fun EventIconDetailSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5BB854)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6B7280),
                modifier = Modifier.size(22.dp)
            )

            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563)
            )
        }
    }
}

// Displays information about the event host.
// Links the event to a User profile and uses profile resources (e.g., profile_image_mountain).
@Composable
private fun HostedBySection(
    event: MeetupEvent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "HOSTED BY",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5BB854)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_image_mountain),
                contentDescription = event.host.displayName,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = event.host.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2933)
            )
        }
    }
}
// Interaction point to view the list of people attending the event.
// Connected to the ParticipantList component when clicked.
@Composable
private fun ParticipantsButton(
    participantStatus: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFD0E8D1)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Groups,
                contentDescription = "Participants",
                tint = Color(0xFF208E4A),
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Participants $participantStatus",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF208E4A)
            )
        }
    }
}

/**
 * A primary action button that allows the user to join the event.
 * Serves as the final call-to-action (CTA) in the event details card.
 *
 * @param onClick Callback triggered when the user clicks the button to join.
 * @param modifier Modifier used to adjust the button's layout or styling.
 */
@Composable
private fun JoinEventButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0368F6),
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Join Event",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Preview provider for the [EventCard] component.
 * It uses [selectedDummyEvent] to demonstrate how the event details,
 * host information, and action buttons appear on a standard mobile screen.
 */
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun EventCardPreview() {
    EventCard(event = selectedDummyEvent)
}