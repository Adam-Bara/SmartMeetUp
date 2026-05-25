package com.example.smartmeetup.ui.events.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.data.dummy.selectedDummyEvent
import com.example.smartmeetup.model.MeetupEvent
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun EventCard(
    event: MeetupEvent,
    modifier: Modifier = Modifier
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EventImagePlaceholder(category = event.category)

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1F2933)
                )

                Text(
                    text = event.previewText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                EventInfoRow(
                    icon = Icons.Outlined.AccessTime,
                    text = event.timeRange
                )

                EventInfoRow(
                    icon = Icons.Outlined.LocationOn,
                    text = event.locationName
                )

                EventInfoRow(
                    icon = Icons.Outlined.People,
                    text = "${event.participantStatus} participants"
                )
            }
        }
    }
}
@Composable
private fun EventImagePlaceholder(
    category: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFD0E8D1))
            .padding(14.dp)
    ) {
        Text(
            text = category,
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFF8FAFB))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF208E4A)
        )

        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 1) 54.dp else 42.dp)
                        .clip(CircleShape)
                        .background(
                            when (index) {
                                0 -> Color(0xFFCCE7FD)
                                1 -> Color(0xFFF6C91D)
                                else -> Color(0xFFB7DAFC)
                            }
                        )
                )
            }
        }
    }
}
@Composable
private fun EventInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF5BB854)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4B5563)
        )
    }
}
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun EventCardPreview() {
    EventCard(event = selectedDummyEvent)
}