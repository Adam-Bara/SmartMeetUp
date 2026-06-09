package com.example.smartmeetup.ui.chat

import androidx.lifecycle.ViewModel
import com.example.smartmeetup.data.repository.ChatRepository
import com.example.smartmeetup.model.ChatMessage
import com.example.smartmeetup.model.Conversation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatUiState(
    val conversations: List<Conversation> = emptyList(),
    val selectedConversation: Conversation? = null,
    val messages: List<ChatMessage> = emptyList(),
    val errorMessage: String? = null
)

class ChatViewModel(
    private val chatRepository: ChatRepository = ChatRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ChatUiState(
            conversations = chatRepository.getConversations()
        )
    )

    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun selectConversation(eventId: Int) {
        val conversation = chatRepository.getConversationByEventId(eventId)
        val messages = chatRepository.getMessagesForEvent(eventId)

        _uiState.value = _uiState.value.copy(
            selectedConversation = conversation,
            messages = messages,
            errorMessage = if (conversation == null) "Conversation not found" else null
        )
    }

    fun sendMessage(eventId: Int, text: String) {
        if (text.isBlank()) return

        val updatedMessages = chatRepository.sendMessage(
            eventId = eventId,
            text = text.trim()
        )

        _uiState.value = _uiState.value.copy(
            messages = updatedMessages,
            errorMessage = null
        )
    }
}

