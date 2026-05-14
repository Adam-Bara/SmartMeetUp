package com.example.smartmeetup.ui.app

// Compose-Import für Composable-Funktionen
import androidx.compose.runtime.Composable

// Import für die Android-Studio-Vorschau
import androidx.compose.ui.tooling.preview.Preview

// App-Theme von SmartMeetUp
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

// Einstiegspunkt für die Compose-Oberfläche der App.
// Diese Funktion kapselt den Hauptaufbau der App und zeigt das MainScaffold an.
@Composable
fun SmartMeetupApp() {
    // MainScaffold enthält die Grundstruktur der App:
    // TopAppBar, Bottom Navigation und den jeweiligen Screen-Inhalt.
    MainScaffold()
}

// Preview-Funktion für Android Studio.
// Damit kann die komplette App-Struktur direkt in der Vorschau angezeigt werden.
@Preview(showBackground = true)
@Composable
fun SmartMeetupAppPreview() {
    // Wendet das App-Theme auf die Vorschau an,
    // damit Farben, Typografie und Material-Design korrekt dargestellt werden.
    SmartMeetUpTheme {
        SmartMeetupApp()
    }
}