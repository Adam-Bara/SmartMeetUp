package com.example.smartmeetup.ui.events.assets

import androidx.annotation.DrawableRes
import com.example.smartmeetup.R
import com.example.smartmeetup.model.EventImageType

@DrawableRes
fun eventImageRes(eventImageType: EventImageType): Int {
    return when (eventImageType) {
        EventImageType.Park -> R.drawable.event_image_park
        EventImageType.Tennis -> R.drawable.event_image_tennis
        EventImageType.Picnic -> R.drawable.event_image_picnic
        EventImageType.Workshop -> R.drawable.event_image_workshop
        EventImageType.Hiking -> R.drawable.event_image_hiking
        EventImageType.Birthday -> R.drawable.event_image_birthday
    }
}