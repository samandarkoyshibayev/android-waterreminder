package com.android.waterreminder.presentation.screens.setting

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.dialog.LanguageDialog
import com.android.waterreminder.ui.dialog.ThemeDialog
import com.android.waterreminder.ui.theme.extendedColors
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SettingScreenContract.ViewModel = getViewModel<SettingScreenViewModel>()
        val uiState = viewModel.collectAsState().value
        val context = LocalContext.current
        val activity = context as? Activity


        LaunchedEffect(Unit) {
            viewModel.onEventDispatcher(SettingScreenContract.Intent.LoadSettings)
        }


        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SettingScreenContract.SideEffect.ShowMessage -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
                SettingScreenContract.SideEffect.RecrateActivity -> {
                    activity?.recreate()
                }
            }
        }
        SettingsScreenContent(
            uiState = uiState, onEventDispatcher = viewModel::onEventDispatcher
        )

        if (uiState.showThemeDialog) {
            ThemeDialog(
                currentTheme = uiState.themeMode,
                onDismiss = {
                    viewModel.onEventDispatcher(SettingScreenContract.Intent.CloseThemeDialog)
                },
                onThemeSelected = { theme ->
                    viewModel.onEventDispatcher(SettingScreenContract.Intent.SelectTheme(theme))
                }
            )
        }
        if (uiState.showLanguageDialog) {
            LanguageDialog(
                currentLanguage = uiState.language,
                onDismiss = {
                    viewModel.onEventDispatcher(SettingScreenContract.Intent.CloseLanguageDialog)
                },
                onLanguageSelected ={ language ->
                    viewModel.onEventDispatcher(SettingScreenContract.Intent.SelectLanguage(language = language))
                }
            )
        }
    }
}

@Composable
fun SettingsScreenContent(
    uiState: SettingScreenContract.UIState,
    onEventDispatcher: (SettingScreenContract.Intent) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 11.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = "Settings", style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                SectionHeader(title = "Reminder Settings")
                Spacer(modifier = Modifier.height(12.dp))

                SettingCard {
                    Column {
                        SettingRowWithSwitch(
                            icon = R.drawable.ic_ring,
                            title = "Enable Reminders",
                            isEnabled = uiState.isReminderEnabled,
                            onToggle = {
                                onEventDispatcher(
                                    SettingScreenContract.Intent.ToggleReminders(
                                        it
                                    )
                                )
                            }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingRow(
                            icon = R.drawable.ic_clock,
                            title = "Reminder Interval",
                            value = "${uiState.reminderInterval} min",
                            onClick = { onEventDispatcher(SettingScreenContract.Intent.OpenReminderMode) },
                            enabled = uiState.isReminderEnabled
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SectionHeader(title = "General Settings")
                Spacer(modifier = Modifier.height(12.dp))

                SettingCard {
                    Column {
                        SettingRow(
                            icon = R.drawable.ic_cup,
                            title = "Daily Intake Goal",
                            value = "${uiState.intakeGoal} ml",
                            onClick = { onEventDispatcher(SettingScreenContract.Intent.OpenComingSoonMessage("Daily Intake Goal dialog is coming soon")) },
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingRow(
                            icon = R.drawable.ic_language,
                            title = "Language",
                            value = uiState.language.toString(),
                            onClick = { onEventDispatcher(SettingScreenContract.Intent.OpenLanguageDialog) },

                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingRow(
                            icon = R.drawable.ic_dark,
                            title = "Theme",
                            value = uiState.themeMode,
                            onClick = { onEventDispatcher(SettingScreenContract.Intent.OpenThemeDialog) },

                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // About Section
                SectionHeader(title = "About")
                Spacer(modifier = Modifier.height(12.dp))

                SettingCard {
                    Column {
                        SettingRow(
                            icon = R.drawable.ic_version,
                            title = "App Version",
                            value = "1.0.0",
                            onClick = { })

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingRow(
                            icon = R.drawable.ic_privacy_policy,
                            title = "Privacy Policy",
                            value = "",
                            onClick = { },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title, style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun SettingCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface   )
    ) {
        content()
    }
}

@Composable
private fun SettingRow(
    icon: Int,
    title: String,
    value: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title, style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value, style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun SettingRowWithSwitch(
    icon: Int, title: String, isEnabled: Boolean, onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title, style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Switch(
            checked = isEnabled, onCheckedChange = onToggle, colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.extendedColors.white,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.extendedColors.white,
                        uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
private fun Divider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier, thickness = 1.dp, color = MaterialTheme.colorScheme.tertiary
    )
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val sampleState = SettingScreenContract.UIState(
        isReminderEnabled = true, reminderInterval = 60, intakeGoal = 2000
    )

    SettingsScreenContent(
        uiState = sampleState,
        {})
}