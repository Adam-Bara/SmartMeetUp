package com.example.smartmeetup.map

import android.content.Context
import android.preference.PreferenceManager
import org.osmdroid.config.Configuration

object OsmDroidConfig {

    fun initialize(context: Context) {
        val applicationContext = context.applicationContext

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
    }
}