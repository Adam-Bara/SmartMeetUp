package com.example.smartmeetup.ui.events.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.data.dummy.selectedDummyEvent
import com.example.smartmeetup.model.MeetupEvent

@Composable
fun EventCard(
    event: MeetupEvent,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "| ${event.category}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = event.previewText,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = event.timeRange,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = event.locationName,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "${event.participantStatus} participants",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    EventCard(event = selectedDummyEvent)
}