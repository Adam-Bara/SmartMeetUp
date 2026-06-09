package com.example.smartmeetup.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.R
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import com.example.smartmeetup.model.ChatMessage
import com.example.smartmeetup.model.User

//ChatScreen receives ChatMessage data from outside, and preview data is explicit
@Composable
fun ChatScreen(
    chatTitle: String,
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ChatBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 118.dp, bottom = 104.dp)
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            messages.forEach { message ->
                ChatMessageBubble(
                    message = message,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        ChatHeader(
            title = chatTitle,
            onBackClick = onBackClick,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        ChatInputBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ChatBackground(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.event_image_park),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.22f //means the image is only 22% visible, so roughly 78% faded out
    )
}

@Composable
private fun ChatHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .background(Color.White.copy(alpha = 0.88f))
            .padding(horizontal = 28.dp)
            .padding(top = 34.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBackClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "Back",
                tint = Color(0xFF5BB854),
                modifier = Modifier.size(30.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier.align(Alignment.Center)
        )

        Image(
            painter = painterResource(id = R.drawable.profile_image_mountain),
            contentDescription = "Chat avatar",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(58.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ChatMessageBubble( //bubble styling is split into tiny helpers
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val bubbleColors = chatBubbleColors(isOwnMessage = message.isOwnMessage)

    Row(
        modifier = modifier,
        horizontalArrangement = chatBubbleAlignment(isOwnMessage = message.isOwnMessage)
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 285.dp),
            shape = chatBubbleShape(isOwnMessage = message.isOwnMessage),
            color = bubbleColors.backgroundColor,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 18.dp,
                    vertical = 14.dp
                )
            ) {
                Text(
                    text = message.sender.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = bubbleColors.senderColor
                )

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.titleMedium,
                    color = bubbleColors.messageColor,
                    modifier = Modifier.padding(top = 8.dp)
                )

                ChatMessageMetaRow(
                    time = message.time,
                    color = bubbleColors.metaColor
                )
            }
        }
    }
}

private data class ChatBubbleColors(
    val backgroundColor: Color,
    val senderColor: Color,
    val messageColor: Color,
    val metaColor: Color
)

private fun chatBubbleColors(isOwnMessage: Boolean): ChatBubbleColors {
    return if (isOwnMessage) {
        ChatBubbleColors(
            backgroundColor = Color(0xFF2EAF3D),
            senderColor = Color.White,
            messageColor = Color.White,
            metaColor = Color.White.copy(alpha = 0.88f)
        )
    } else {
        ChatBubbleColors(
            backgroundColor = Color(0xFFE8F4FF),
            senderColor = Color(0xFF1285F5),
            messageColor = Color(0xFF111827),
            metaColor = Color(0xFF6B7280)
        )
    }
}

private fun chatBubbleAlignment(isOwnMessage: Boolean): Arrangement.Horizontal {
    return if (isOwnMessage) Arrangement.End else Arrangement.Start
}

private fun chatBubbleShape(isOwnMessage: Boolean): RoundedCornerShape {
    return if (isOwnMessage) {
        RoundedCornerShape(
            topStart = 22.dp,
            topEnd = 22.dp,
            bottomStart = 22.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 22.dp,
            topEnd = 22.dp,
            bottomStart = 4.dp,
            bottomEnd = 22.dp
        )
    }
}

@Composable
private fun ChatMessageMetaRow(
    time: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )

        Text(
            text = "  ✓✓",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}
@Composable
private fun ChatInputBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.92f))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.ime)
            .padding(horizontal = 28.dp)
            .padding(top = 14.dp, bottom = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Add attachment",
            tint = Color(0xFF5BB854),
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.padding(start = 14.dp))

        Surface(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            color = Color(0xFFF2F2F4)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Type a message...",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF8A8F98)
                )
            }
        }
    }
}
private val previewBrookeUser = User(
    id = 20,
    displayName = "Brooke",
    username = "@brooke"
)

private val previewCurrentUser = User(
    id = 99,
    displayName = "Lucas",
    username = "@lucas"
)

private val previewChatMessages = listOf(
    ChatMessage(
        id = 1,
        eventId = 123,
        sender = previewBrookeUser,
        text = "Hey Lucas!",
        time = "10:30 AM",
        isOwnMessage = false
    ),
    ChatMessage(
        id = 2,
        eventId = 123,
        sender = previewCurrentUser,
        text = "Hi Brooke!",
        time = "10:31 AM",
        isOwnMessage = true
    ),
    ChatMessage(
        id = 3,
        eventId = 123,
        sender = previewBrookeUser,
        text = "No worries. Let me know if you need any help 🙂",
        time = "10:32 AM",
        isOwnMessage = false
    )
)


@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun ChatScreenPreview() {
    SmartMeetUpTheme {
        ChatScreen(
            chatTitle = "Brooke Davis",
            messages = previewChatMessages
        )
    }
}