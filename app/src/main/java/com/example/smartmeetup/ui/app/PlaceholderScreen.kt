// File Purpose: A reusable screen for displaying placeholder content for unimplemented features.
// Communication: MainScaffold, SmartMeetupApp.
// Owner: Daria Zecha

package com.example.smartmeetup.ui.app

// Layout-Imports für Spaltenlayout, Abstände, Vollbildgröße und Innenabstand
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

// Material-3-Imports für Theme und Textdarstellung
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Compose-Import für Composable-Funktionen
import androidx.compose.runtime.Composable

// UI-Imports für Ausrichtung, Modifier und Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// App-Theme
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

// Wiederverwendbarer Platzhalter-Screen.
// Dieser Screen wird genutzt, solange echte Screens wie Map, Profil oder Notifications
// noch nicht vollständig implementiert sind.
@Composable
fun PlaceholderScreen(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    // Column ordnet die enthaltenen UI-Elemente vertikal untereinander an.
    Column(
        modifier = modifier
            // Nimmt den gesamten verfügbaren Platz ein.
            .fillMaxSize()
            // Fügt rundherum einen Innenabstand hinzu.
            .padding(24.dp),

        // Zentriert die Inhalte horizontal innerhalb der Column.
        horizontalAlignment = Alignment.CenterHorizontally,

        // Zentriert die Inhalte vertikal innerhalb des verfügbaren Platzes.
        verticalArrangement = Arrangement.Center
    ) {
        // Überschrift des Platzhalter-Screens
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        // Beschreibungstext unterhalb der Überschrift
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            // Abstand nach oben, damit Titel und Beschreibung nicht direkt aneinanderkleben.
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

// Preview-Funktion für Android Studio.
// Damit kann der PlaceholderScreen direkt in der Vorschau getestet werden.
@Preview(showBackground = true)
@Composable
fun PlaceholderScreenPreview() {
    SmartMeetUpTheme {
        PlaceholderScreen(
            title = "Map",
            text = "Here we will show nearby meetup events on a map."
        )
    }
}