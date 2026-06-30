// File Purpose: Editable UI form for creating a new meetup event.
// Communication: MainScaffold passes CreateEventFormState to EventViewModel.
// Owner: Kaida

package com.example.smartmeetup.ui.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.R
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme

data class CreateEventFormState(
    val title: String,
    val category: String,
    val description: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val locationName: String,
    val participantLimit: Int
)

@Composable
fun CreateEventScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onPublishClick: (CreateEventFormState) -> Unit = {}
) {
    var title by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var startTime by rememberSaveable { mutableStateOf("") }
    var endTime by rememberSaveable { mutableStateOf("") }
    var locationName by rememberSaveable { mutableStateOf("") }
    var participantLimitText by rememberSaveable { mutableStateOf("") }

    val participantLimit = participantLimitText.toIntOrNull() ?: 8
    val canPublish = title.isNotBlank() && locationName.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 118.dp)
        ) {
            CreateEventHeader(
                onCloseClick = onCloseClick
            )

            CreateEventProgressDots(
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                CreateEventTextFieldRow(
                    label = "EVENT NAME",
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter event name",
                    icon = Icons.AutoMirrored.Outlined.List
                )

                CreateEventTextFieldRow(
                    label = "EVENT TYPE",
                    value = category,
                    onValueChange = { category = it },
                    placeholder = "Study, sport, picnic...",
                    icon = Icons.Outlined.Sell
                )

                CreateEventTextFieldRow(
                    label = "DESCRIPTION",
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Add a description",
                    icon = Icons.Outlined.Description,
                    minLines = 3
                )

                CreateEventTextFieldRow(
                    label = "DATE",
                    value = date,
                    onValueChange = { date = it },
                    placeholder = "Today, 30.06.2026...",
                    icon = Icons.Outlined.AccessTime
                )

                CreateEventTextFieldRow(
                    label = "START TIME",
                    value = startTime,
                    onValueChange = { startTime = it },
                    placeholder = "18:00",
                    icon = Icons.Outlined.AccessTime
                )

                CreateEventTextFieldRow(
                    label = "END TIME",
                    value = endTime,
                    onValueChange = { endTime = it },
                    placeholder = "20:00",
                    icon = Icons.Outlined.AccessTime
                )

                CreateEventTextFieldRow(
                    label = "WHERE",
                    value = locationName,
                    onValueChange = { locationName = it },
                    placeholder = "Add location",
                    icon = Icons.Outlined.LocationOn
                )

                CreateEventTextFieldRow(
                    label = "PARTICIPANTS LIMIT",
                    value = participantLimitText,
                    onValueChange = { newValue ->
                        if (newValue.all { character -> character.isDigit() }) {
                            participantLimitText = newValue
                        }
                    },
                    placeholder = "8",
                    icon = Icons.Outlined.People,
                    keyboardType = KeyboardType.Number,
                    showDivider = false
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0.94f),
                            Color.White
                        )
                    )
                )
        )

        PublishEventButton(
            text = "Publish Event",
            enabled = canPublish,
            onClick = {
                onPublishClick(
                    CreateEventFormState(
                        title = title.trim(),
                        category = category.ifBlank { "Meetup" }.trim(),
                        description = description.ifBlank { "No description added yet." }.trim(),
                        date = date.ifBlank { "Today" }.trim(),
                        startTime = startTime.ifBlank { "18:00" }.trim(),
                        endTime = endTime.ifBlank { "20:00" }.trim(),
                        locationName = locationName.trim(),
                        participantLimit = participantLimit
                    )
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 34.dp)
                .padding(bottom = 28.dp)
        )
    }
}

@Composable
private fun CreateEventHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 14.dp)
            .padding(top = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(212.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.event_image_upload_bg),
                contentDescription = "Upload event picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(26.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 2.dp, top = 0.dp)
                    .size(42.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onCloseClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF111827),
                    modifier = Modifier.size(32.dp)
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 16.dp)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.78f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CloudUpload,
                    contentDescription = "Upload event picture",
                    tint = Color(0xFF0368F6),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Text(
            text = "Upload pictures for event",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
        )

        Text(
            text = "Add photos to make your event stand out",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6B7280),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        )
    }
}

@Composable
private fun CreateEventProgressDots(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (index == 0) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == 0) Color(0xFF0368F6) else Color(0xFFC4C6CA)
                    )
            )
        }
    }
}

@Composable
private fun CreateEventTextFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    showDivider: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEAF7EE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF20A347),
                modifier = Modifier.size(22.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 18.dp)
                .weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF208E4A)
            )

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(text = placeholder)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                minLines = minLines,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0368F6),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 13.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )
            }
        }
    }
}

@Composable
private fun PublishEventButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = if (enabled) {
        Color(0xFF0368F6)
    } else {
        Color(0xFFC4C6CA)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        color = buttonColor
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
fun CreateEventScreenPreview() {
    SmartMeetUpTheme {
        CreateEventScreen()
    }
}