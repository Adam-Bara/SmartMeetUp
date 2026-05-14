package com.example.smartmeetup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartmeetup.ui.app.SmartMeetupApp
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartMeetUpTheme {
                SmartMeetupApp()
            }
        }
    }
}