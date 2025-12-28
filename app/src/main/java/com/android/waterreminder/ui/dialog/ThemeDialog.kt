package com.android.waterreminder.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.waterreminder.R

@Composable
fun ThemeDialog(
    currentTheme: String,
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = "Choose Theme",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Select your preferred theme mode",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Theme Options
                ThemeOption(
                    icon = R.drawable.ic_light,
                    title = "Light",
                    description = "Bright and clear",
                    isSelected = currentTheme == "Light",
                    onClick = { onThemeSelected("Light") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ThemeOption(
                    icon = R.drawable.ic_dark,
                    title = "Dark",
                    description = "Easy on the eyes",
                    isSelected = currentTheme == "Dark",
                    onClick = { onThemeSelected("Dark") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ThemeOption(
                    icon = R.drawable.ic_system,
                    title = "System",
                    description = "Follow system setting",
                    isSelected = currentTheme == "System",
                    onClick = { onThemeSelected("System") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Cancel Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cancel",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeOption(
    icon: Int,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isSelected) {
                            MaterialTheme.colorScheme.surface
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    )
                }
            }

            // Check Icon
            if (isSelected) {
                Icon(
                    painter = painterResource(R.drawable.ic_checkmark),
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
