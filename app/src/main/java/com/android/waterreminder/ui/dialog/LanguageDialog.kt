package com.android.waterreminder.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.android.waterreminder.util.LanguageEnum

@Composable
fun LanguageDialog(
    currentLanguage: LanguageEnum,
    onDismiss: () -> Unit,
    onLanguageSelected: (LanguageEnum) -> Unit
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
                    text = "Choose Language",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Select your preferred language",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Theme Options
                LanguageOption(
                    icon = R.drawable.ic_en,
                    title = "EN",
                    description = "English",
                    isSelected = currentLanguage == LanguageEnum.EN,
                    onClick = { onLanguageSelected(LanguageEnum.EN) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                LanguageOption(
                    icon = R.drawable.ic_uz,
                    title = "UZ",
                    description = "Oʻzbek (Oʻzbekiston)",
                    isSelected = currentLanguage == LanguageEnum.UZ,
                    onClick = { onLanguageSelected(LanguageEnum.UZ) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                LanguageOption(
                    icon = R.drawable.ic_ru,
                    title = "RU",
                    description = "Русский (Россия)",
                    isSelected = currentLanguage == LanguageEnum.RU,
                    onClick = { onLanguageSelected(LanguageEnum.RU) }
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
fun LanguageOption(
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
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
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