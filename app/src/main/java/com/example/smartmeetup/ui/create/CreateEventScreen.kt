// File Purpose: UI for the multi-step form to create a new meetup event.
// Communication: MainScaffold, MeetupEvent (future), MapViewModel (future).
// Owner: Daria Zecha

package com.example.smartmeetup.ui.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmeetup.R
import com.example.smartmeetup.ui.theme.SmartMeetUpTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.ui.graphics.Brush

// Orchestrates the multi-step event creation flow.
// It integrates visual components like the image header and progress dots
// with functional input rows, providing a unified entry point for users
// to add new meetups to the system.
@Composable
fun CreateEventScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onPublishClick: () -> Unit = {}
) {
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
            // Header with image upload and close button
            CreateEventHeader(
                onCloseClick = onCloseClick
            )

            // Step indicator
            CreateEventProgressDots(
                modifier = Modifier.padding(top = 8.dp)
            )

            // Input fields for event details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 34.dp)
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                CreateEventFieldRow(
                    label = "EVENT NAME",
                    value = "Enter event name",
                    icon = Icons.AutoMirrored.Outlined.List
                )

                CreateEventFieldRow(
                    label = "EVENT TYPE",
                    value = "Select type",
                    icon = Icons.Outlined.Sell,
                    showDropdown = true
                )

                CreateEventFieldRow(
                    label = "DESCRIPTION",
                    value = "Add a description",
                    icon = Icons.Outlined.Description
                )

                CreateEventFieldRow(
                    label = "WHEN",
                    value = "select date and time",
                    icon = Icons.Outlined.AccessTime
                )

                CreateEventFieldRow(
                    label = "WHERE",
                    value = "Add location",
                    icon = Icons.Outlined.LocationOn
                )

                CreateEventFieldRow(
                    label = "PARTICIPANTS LIMIT",
                    value = "Set participant limit",
                    icon = Icons.Outlined.People,
                    showDivider = false
                )
            }
        }

        // Sticky bottom action button with a soft white gradient background
        Box(
            modifier = Modifier
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
            onClick = onPublishClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 34.dp)
                .padding(bottom = 28.dp)
        )
    }
}

// Header section that provides an interface for uploading event images.
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

// Visual indicator showing the progress of the multi-step event creation form.
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

// Reusable template for event input fields (e.g., Name, Category, Date).
// It features a consistent icon-label layout and an optional dropdown indicator
// for fields that require a selection (connected to logic in future ViewModels).
@Composable
private fun CreateEventFieldRow(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    showDropdown: Boolean = false,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.weight(1f)
                )

                if (showDropdown) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "Select event type",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

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

// The primary action button to finalize event creation.
// Positioned at the bottom with a gradient overlay to maintain visibility
// while scrolling through the form details.
@Composable
private fun PublishEventButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0368F6)
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

/**
 * Preview for the [CreateEventScreen] composable.
 * Displays the event creation flow in a standard mobile device format (390x844 dp)
 * to verify the layout, input fields, and sticky action button.
 */
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