package com.example.smartmeetup.ui.events.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartmeetup.R
import com.example.smartmeetup.data.dummy.selectedDummyEvent
import com.example.smartmeetup.model.User
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.height

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
        ParticipantListHeader(
            participantStatus = participantStatus,
            onBackClick = onBackClick
        )
//orEachIndexed loops through the whole participant list and gives both the index and the participant
        participants.forEachIndexed { index, participant ->
            ParticipantRow(
                participant = participant,
                avatarResId = participantAvatarForIndex(index)
            )
        }
    }
}

@Composable
private fun ParticipantListHeader(
    participantStatus: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBackClick
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color(0xFF5BB854),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Back",
                color = Color(0xFF5BB854),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = "Participants",
            modifier = Modifier.align(Alignment.Center),
            color = Color(0xFF111827),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    color = Color(0xFFD0E8D1),
                    shape = RoundedCornerShape(13.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Participants",
                tint = Color(0xFF5BB854),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = participantStatus,
                color = Color(0xFF5BB854),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@DrawableRes //avatars is our reusable pool of profile images. index % avatars.size means: once we reach the end of the image list, start again from the beginning
private fun participantAvatarForIndex(index: Int): Int {
    val avatars = listOf(
        R.drawable.profile_image_mountain,
        R.drawable.profile_image_flower,
        R.drawable.profile_image_wave,
        R.drawable.profile_image_tree,
        R.drawable.profile_image_hotairbaloon,
        R.drawable.profile_image_cactus,
        R.drawable.profile_image_star
    )

    return avatars[index % avatars.size]
}
@Composable
private fun ParticipantRow(
    participant: User,
    @DrawableRes avatarResId: Int
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = avatarResId),
                contentDescription = "Avatar of ${participant.displayName}",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(26.dp))

            Text(
                text = participant.displayName,
                color = Color(0xFF111827),
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium
            )
        }
//HorizontalDivider is not a black line; it is a pale grey separator starting after the avatar area
        HorizontalDivider(
            modifier = Modifier.padding(start = 90.dp),
            thickness = 1.dp,
            color = Color(0xFFE5E7EB)
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