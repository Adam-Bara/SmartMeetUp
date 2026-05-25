package com.example.smartmeetup.ui.events.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.model.User
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.smartmeetup.R
import com.example.smartmeetup.data.dummy.selectedDummyEvent

@Composable
fun ParticipantList(
    participants: List<User>,
    participantStatus: String,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFB))
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(text = "Teilnehmerliste")
        Text(text = participantStatus)
        Text(text = "Anzahl: ${participants.size}")

        if (participants.isNotEmpty()) {
            ParticipantRow(
                participant = participants.first(),
                avatarResId = R.drawable.profile_image_flower
            )
        }
    }
}

@Composable
private fun ParticipantRow(
    participant: User,
    @DrawableRes avatarResId: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = avatarResId),
            contentDescription = "Avatar of ${participant.displayName}",
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = participant.displayName
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ParticipantListPreview() {
    ParticipantList(
        participants = selectedDummyEvent.participants,
        participantStatus = "10 / 15",
        onBackClick = {}
    )
}