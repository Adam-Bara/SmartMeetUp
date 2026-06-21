// File Purpose: UI for the user profile, displaying statistics, bio, and interests.
// Communication: MainScaffold, User (future).
// Owner: Rina

package com.example.smartmeetup.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeaderCard(
            name = "Max Mustermann",
            username = "@maxmeetup",
            location = "Berlin, Deutschland",
            modifier = Modifier.fillMaxWidth()
        )

        ProfileStatsCard(
            joinedEvents = "12",
            hostedEvents = "3",
            modifier = Modifier.fillMaxWidth()
        )

        ProfileInfoCard(
            title = "Über mich",
            text = "Ich nutze SmartMeetUp, um spontane Events in meiner Nähe zu finden und neue Leute kennenzulernen.",
            modifier = Modifier.fillMaxWidth()
        )

        ProfileInterestsCard(
            interests = listOf(
                "Sport",
                "Gaming",
                "Musik",
                "Food",
                "Outdoor",
                "Kino"
            ),
            modifier = Modifier.fillMaxWidth()
        )

        ProfileInfoCard(
            title = "Account",
            text = "Profilinformationen, Einstellungen und Privatsphäre können später hier verwaltet werden.",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    name: String,
    username: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileAvatar()

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6E6E73)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6E6E73)
            )
        }
    }
}

@Composable
private fun ProfileAvatar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(Color(0xFFE6F0FF)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profilbild",
            modifier = Modifier.size(54.dp),
            tint = Color(0xFF0368F6)
        )
    }
}

@Composable
private fun ProfileStatsCard(
    joinedEvents: String,
    hostedEvents: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileStatItem(
                value = joinedEvents,
                label = "Beigetreten"
            )

            VerticalDivider()

            ProfileStatItem(
                value = hostedEvents,
                label = "Erstellt"
            )
        }
    }
}

@Composable
private fun ProfileStatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6E6E73)
        )
    }
}

@Composable
private fun VerticalDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier
            .height(40.dp)
            .width(1.dp),
        color = Color(0xFFE5E5EA)
    )
}

@Composable
private fun ProfileInfoCard(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6E6E73)
            )
        }
    }
}

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun ProfileInterestsCard(
    interests: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Interessen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                interests.forEach { interest ->
                    AssistChip(
                        onClick = {},
                        enabled = false,
                        label = {
                            Text(text = interest)
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            disabledContainerColor = Color(0xFFE6F0FF),
                            disabledLabelColor = Color(0xFF0368F6)
                        ),
                        border = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SmartMeetUpTheme {
        Surface {
            ProfileScreen()
        }
    }
}