package com.example.smartmeetup.ui.chat

import com.example.smartmeetup.model.Conversation
import com.example.smartmeetup.ui.events.assets.eventImageRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.R
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.ui.events.assets.eventImageRes

/**
 * Displays the message overview screen.
 *
 * This composable renders a list of private or event-related conversations.
 * It does not perform navigation itself. Row clicks are exposed through onMessageClick,
 * so the parent navigation layer can later open ChatScreen.
 *
 * The modifier parameter follows Compose convention and lets a parent composable
 * control external layout concerns such as padding, size, or background.
 */
@Composable
fun AllMessagesScreen(
    conversations: List<Conversation>,
    modifier: Modifier = Modifier,
    onMessageClick: (Conversation) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 48.dp)
    ) {
        MessagesHeader()

        SearchBar(
            modifier = Modifier.padding(top = 28.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            items(conversations) { conversation ->
                MessageOverviewCard(
                    item = conversation,
                    onClick = { onMessageClick(conversation) }
                )
            }
        }
    }
}


@Composable
private fun MessagesHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Messages",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Your event conversations",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF8A8F98),
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.profile_image_mountain),
            contentDescription = "Profile avatar",
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        shape = RoundedCornerShape(31.dp),
        color = Color(0xFFF3F4FA)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color(0xFF8A8F98),
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = "Search events or messages",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF7B8190),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun MessageOverviewCard(
    item: Conversation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(104.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE9EDF3)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = eventImageRes(item.imageType)),
                contentDescription = item.title,
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, end = 6.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.senderName,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7B8190),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = item.previewText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF7B8190),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 7.dp)
                )
            }

            Column(
                modifier = Modifier
                    .width(74.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7B8190),
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    contentDescription = "Open chat",
                    tint = Color(0xFF0368F6),
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

private val previewConversations = listOf(
    Conversation(
        id = 1,
        eventId = 123,
        title = "Project Kickoff Meetup",
        senderName = "Brooke Davis",
        previewText = "Sounds good! See you there.",
        date = "May 20, 2025",
        imageType = EventImageType.Park
    ),
    Conversation(
        id = 2,
        eventId = 125,
        title = "Tennis Tournament",
        senderName = "Lucas Smith",
        previewText = "Great match yesterday!",
        date = "May 18, 2025",
        imageType = EventImageType.Tennis
    )
)
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun AllMessagesScreenPreview() {
    SmartMeetUpTheme {
        AllMessagesScreen(
            conversations = previewConversations,
            onMessageClick = {}
        )
    }
}

//Adams files