package com.example.smartmeetup

// Bundle enthält gespeicherte Zustände, die beim Start der Activity übergeben werden können.
import android.os.Bundle

// Basis-Klasse für eine Android-Activity mit Unterstützung für Jetpack Compose.
import androidx.activity.ComponentActivity

// Ermöglicht es, Compose-Inhalte als UI der Activity zu setzen.
import androidx.activity.compose.setContent

// Aktiviert Edge-to-Edge-Darstellung, sodass die App bis an die Systemleisten reicht.
import androidx.activity.enableEdgeToEdge

// Haupt-Composable der App
import com.example.smartmeetup.ui.app.SmartMeetupApp

// App-Theme von SmartMeetUp
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

// Map Stuff
import androidx.activity.enableEdgeToEdge
import com.example.smartmeetup.map.OsmDroidConfig

// MainActivity ist der Einstiegspunkt der Android-App.
// Sie wird beim Start der App geöffnet.
class MainActivity : ComponentActivity() {

    // onCreate wird aufgerufen, wenn die Activity erstellt wird.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SmartMeetUpTheme {
                // Startet die eigentliche App-Oberfläche.
                SmartMeetupApp()
            }
        }
    }
}