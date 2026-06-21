package com.example.smartmeetup.model
 //this file is the preview model for the message overview screen later in that dynamically updates for AllMessageScreen preview

data class Conversation(
    val id: Int, //identifies the conversation itself
    val eventId: Int, //connects the conversation to the event
    val title: String, //is what appears in AllMessagesScreen, usually the event title
    val senderName: String, //name shown in the preview.
    val previewText: String, //short visible last-message snippet
    val date: String, //displayed time/date
    val imageType: EventImageType //the screen choose the matching event image through your existing image helper
)