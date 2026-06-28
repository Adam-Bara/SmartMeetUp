package com.example.smartmeetup.data.repository
//this file it removes chat data responsibility from the UI screens
//AllMessagesScreen should later get Conversation data from here through the ViewModel
//ChatScreen should later get ChatMessage data from here through the ViewModel
import com.example.smartmeetup.model.ChatMessage
import com.example.smartmeetup.model.Conversation
import com.example.smartmeetup.model.EventImageType
import com.example.smartmeetup.model.User

class ChatRepository {

    private val currentUser = User(
        id = 99,
        displayName = "Lucas",
        username = "@lucas"
    )
    private val brookeUser = User(
        id = 20,
        displayName = "Brooke",
        username = "@brooke"
    )

    private val conversations = listOf(
        Conversation(
            id = 1,
            eventId = 123,
            title = "Football in the Park",
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
        ),
        Conversation(
            id = 3,
            eventId = 124,
            title = "Study Session",
            senderName = "Sarah Lee",
            previewText = "Here is the resource I mentioned.",
            date = "May 5, 2025",
            imageType = EventImageType.Workshop
        ),
        Conversation(
            id = 4,
            eventId = 126,
            title = "Hiking Trip",
            senderName = "Mike Brown",
            previewText = "The trail was amazing!",
            date = "Apr 28, 2025",
            imageType = EventImageType.Hiking
        )
    )

    private val messages = mutableListOf(
        ChatMessage(
            id = 1,
            eventId = 123,
            sender = brookeUser,
            text = "Hey Lucas!",
            time = "10:30 AM",
            isOwnMessage = false
        ),
        ChatMessage(
            id = 2,
            eventId = 123,
            sender = brookeUser,
            text = "How is your project going?",
            time = "10:30 AM",
            isOwnMessage = false
        ),
        ChatMessage(
            id = 3,
            eventId = 123,
            sender = currentUser,
            text = "Hi Brooke!",
            time = "10:31 AM",
            isOwnMessage = true
        ),
        ChatMessage(
            id = 4,
            eventId = 123,
            sender = currentUser,
            text = "It is going well. Thanks for asking!",
            time = "10:31 AM",
            isOwnMessage = true
        ),
        ChatMessage(
            id = 5,
            eventId = 123,
            sender = User(id = 20, displayName = "Brooke", username = "@brooke"),
            text = "No worries. Let me know if you need help.",
            time = "10:32 AM",
            isOwnMessage = false
        ),
        ChatMessage(
            id = 6,
            eventId = 123,
            sender = currentUser,
            text = "You are the best!",
            time = "10:32 AM",
            isOwnMessage = true
        )
    )

    fun getConversations(): List<Conversation> {
        return conversations
    }

    fun getConversationByEventId(eventId: Int): Conversation? {
        return conversations.find { conversation ->
            conversation.eventId == eventId
        }
    }

    fun getMessagesForEvent(eventId: Int): List<ChatMessage> {
        return messages.filter { message ->
            message.eventId == eventId
        }
    }

    fun sendMessage(eventId: Int, text: String): List<ChatMessage> {
        val newMessage = ChatMessage(
            id = messages.size + 1,
            eventId = eventId,
            sender = currentUser,
            text = text,
            time = "Now",
            isOwnMessage = true
        )

        messages.add(newMessage)

        return getMessagesForEvent(eventId)
    }
}