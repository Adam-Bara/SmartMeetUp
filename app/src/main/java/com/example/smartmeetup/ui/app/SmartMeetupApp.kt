package com.example.smartmeetup.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

@Composable
fun SmartMeetupApp() {
    MainScaffold()
}

@Preview(showBackground = true)
@Composable
fun SmartMeetupAppPreview() {
    SmartMeetUpTheme {
        SmartMeetupApp()
    }
}