// File Purpose: UI for an active chat conversation, showing message bubbles, background, and input bar.
// Communication: AllMessagesScreen, EventJoinedScreen, ChatUiMessage.
// Owner: Adam

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

/**
 * Displays a chat conversation UI.
 *
 * This composable only renders the chat screen.
 * Navigation is kept outside and exposed through callbacks such as onBackClick.
 * Later this screen can be opened from EventJoinedScreen or AllMessagesScreen.
 *
 * The modifier parameter follows Compose convention and lets a parent composable
 * control external layout concerns such as padding, size, or background.
 */
@Composable
fun ChatScreen(
    chatTitle: String = "Brooke Davis",
    messages: List<ChatUiMessage> = sampleChatMessages,
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

/**
 * Renders an individual chat message bubble.
 *
 * The bubble's appearance changes based on whether the message was sent by the current user
 * or another participant:
 * - Own messages are aligned to the end (right) with a green background.
 * - Other messages are aligned to the start (left) with a light blue background.
 * - The corner radii are adjusted to create a "tail" effect pointing towards the sender's side.
 *
 * @param message The [ChatUiMessage] data containing sender info, text, and timestamp.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
private fun ChatMessageBubble(
    message: ChatUiMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (message.isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 285.dp),
            shape = if (message.isOwnMessage) {
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
            },
            color = if (message.isOwnMessage) Color(0xFF2EAF3D) else Color(0xFFE8F4FF),
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
                    text = message.senderName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (message.isOwnMessage) Color.White else Color(0xFF1285F5)
                )

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (message.isOwnMessage) Color.White else Color(0xFF111827),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (message.isOwnMessage) {
                            Color.White.copy(alpha = 0.85f)
                        } else {
                            Color(0xFF6B7280)
                        }
                    )

                    Text(
                        text = "  ✓✓",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (message.isOwnMessage) {
                            Color.White.copy(alpha = 0.9f)
                        } else {
                            Color(0xFF6B7280)
                        }
                    )
                }
            }
        }
    }
}

/**
 * A bottom bar for chat message input.
 *
 * This composable provides a text input area and an attachment button. It handles
 * keyboard insets using [WindowInsets.ime] and ensures padding for the system
 * navigation bar.
 *
 * @param modifier The [Modifier] to be applied to the input bar.
 */
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

/**
 * Data model representing a single message in the chat interface.
 *
 * @property senderName The name of the user who sent the message.
 * @property text The actual content of the message.
 * @property time The formatted string representing when the message was sent (e.g., "10:30 AM").
 * @property isOwnMessage Boolean flag to determine if the message was sent by the current user,
 * used to control the alignment and color of the message bubble.
 */
data class ChatUiMessage(
    val senderName: String,
    val text: String,
    val time: String,
    val isOwnMessage: Boolean
)

/**
 * A sample list of [ChatUiMessage] instances used for previewing the [ChatScreen]
 * and as default data for the conversation UI.
 */
private val sampleChatMessages = listOf(
    ChatUiMessage(
        senderName = "Brooke",
        text = "Hey Lucas!",
        time = "10:30 AM",
        isOwnMessage = false
    ),
    ChatUiMessage(
        senderName = "Brooke",
        text = "How's your project going?",
        time = "10:30 AM",
        isOwnMessage = false
    ),
    ChatUiMessage(
        senderName = "Lucas",
        text = "Hi Brooke!",
        time = "10:31 AM",
        isOwnMessage = true
    ),
    ChatUiMessage(
        senderName = "Lucas",
        text = "It's going well. Thanks for asking!",
        time = "10:31 AM",
        isOwnMessage = true
    ),
    ChatUiMessage(
        senderName = "Brooke",
        text = "No worries. Let me know if you need any help 🙂",
        time = "10:32 AM",
        isOwnMessage = false
    ),
    ChatUiMessage(
        senderName = "Lucas",
        text = "You're the best!",
        time = "10:32 AM",
        isOwnMessage = true
    )
)

/**
 * Provides a preview of the [ChatScreen] within the [SmartMeetUpTheme].
 * It displays the chat interface with sample messages on a device with dimensions 390x844dp.
 */
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun ChatScreenPreview() {
    SmartMeetUpTheme {
        ChatScreen()
    }
}