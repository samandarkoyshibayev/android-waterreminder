package com.android.waterreminder.presentation.screens.edit

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.theme.extendedColors
import com.android.waterreminder.ui.theme.primary
import com.android.waterreminder.ui.theme.textColor1
import com.android.waterreminder.ui.theme.white
import org.orbitmvi.orbit.compose.collectAsState

class EditScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: EditScreenContract.ViewModel = getViewModel<EditScreenViewModel>()
        val uiState = viewModel.collectAsState().value

        EditScreenContent(
            uiState = uiState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@Composable
private fun EditScreenContent(
    uiState: EditScreenContract.UIState,
    onEventDispatcher: (EditScreenContract.Intent) -> Unit
) {
    var showWeightPicker by remember { mutableStateOf(false) }
    var showHeightPicker by remember { mutableStateOf(false) }
    var showWakeTimePicker by remember { mutableStateOf(false) }
    var showSleepTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp)
            ) {

                Box(
                    modifier = Modifier
                        .padding(top = 11.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(20.dp)
                            .clickable {
                                onEventDispatcher(EditScreenContract.Intent.Back)
                            },
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = primary
                    )
                }

                // Avatar Selection Card
                AvatarSelectionCard(
                    selectedGender = uiState.gender,
                    onGenderSelect = { onEventDispatcher(EditScreenContract.Intent.OnGenderChange(it)) },
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Personal Information Section
                SectionHeader(
                    title = "Personal Information",
                    icon = R.drawable.ic_personal_information
                )

                Spacer(modifier = Modifier.height(12.dp))

                EditCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // First Name
                        InputField(
                            label = "First Name",
                            value = uiState.firstName,
                            onValueChange = {
                                onEventDispatcher(EditScreenContract.Intent.OnFirstNameChange(it))
                            },
                            placeholder = "Enter first name",
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Last Name
                        InputField(
                            label = "Last Name",
                            value = uiState.lastName,
                            onValueChange = {
                                onEventDispatcher(EditScreenContract.Intent.OnLastNameChange(it))
                            },
                            placeholder = "Enter last name",
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Age
                        InputField(
                            label = "Age",
                            value = uiState.age,
                            onValueChange = {
                                onEventDispatcher(EditScreenContract.Intent.OnAgeChange(it))
                            },
                            placeholder = "Enter age",
                            keyboardType = KeyboardType.Number
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Body Metrics Section
                SectionHeader(title = "Body Metrics", icon = R.drawable.ic_bmi)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EditCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showWeightPicker = true }
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Weight",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textColor1
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = uiState.weight.toString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "kg",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textColor1
                                    ),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                    }

                    EditCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showHeightPicker = true }
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Height",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textColor1
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = uiState.height.toString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "cm",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textColor1
                                    ),
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Schedule Section
                SectionHeader(title = "Daily Schedule", icon = R.drawable.ic_reminder_iterval)

                Spacer(modifier = Modifier.height(12.dp))

                EditCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        TimePickerField(
                            label = "Wake Up Time",
                            value = uiState.wakeUpTime,
                            onClick = { showWakeTimePicker = true },
                            color = Color(0xFFFFB74D)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TimePickerField(
                            label = "Sleep Time",
                            value = uiState.sleepTime,
                            onClick = { showSleepTimePicker = true },
                            color = Color(0xFF7E57C2)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Floating Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
            ) {
                Button(
                    onClick = {
                        onEventDispatcher(EditScreenContract.Intent.SaveChanges)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.horizontalGradient(
                                colors = listOf( MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer)
                            )),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Save Changes",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = white
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Weight Picker Dialog
    if (showWeightPicker) {
        NumberPickerDialog(
            title = "Select Weight",
            value = uiState.weight,
            unit = "kg",
            range = 30..180,
            onValueChange = {
                onEventDispatcher(EditScreenContract.Intent.OnWeightChange(it.toString()))
            },
            onDismiss = { showWeightPicker = false }
        )
    }

    // Height Picker Dialog
    if (showHeightPicker) {
        NumberPickerDialog(
            title = "Select Height",
            value = uiState.height,
            unit = "cm",
            range = 100..250,
            onValueChange = {
                onEventDispatcher(EditScreenContract.Intent.OnHeightChange(it.toString()))
            },
            onDismiss = { showHeightPicker = false }
        )
    }

    // Wake Time Picker Dialog
    if (showWakeTimePicker) {
        TimePickerDialog(
            title = "Select Wake Up Time",
            value = uiState.wakeUpTime,
            onValueChange = {
                onEventDispatcher(EditScreenContract.Intent.OnWakeUpTimeChange(it))
            },
            onDismiss = { showWakeTimePicker = false }
        )
    }

    // Sleep Time Picker Dialog
    if (showSleepTimePicker) {
        TimePickerDialog(
            title = "Select Sleep Time",
            value = uiState.sleepTime,
            onValueChange = {
                onEventDispatcher(EditScreenContract.Intent.OnSleepTimeChange(it))
            },
            onDismiss = { showSleepTimePicker = false }
        )
    }
}

@Composable
private fun NumberPickerDialog(
    title: String,
    value: Int,
    unit: String,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val listState = rememberLazyListState(
                    initialFirstVisibleItemIndex = (value - range.first).coerceIn(0, range.count() - 1)
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(range.toList()) { number ->
                        val isSelected = number == value
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) {
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                            )
                                        )
                                    } else {
                                        Brush.horizontalGradient(
                                            colors = listOf(Color.Transparent, Color.Transparent)
                                        )
                                    }
                                )
                                .clickable {
                                    onValueChange(number)
                                }
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = "$number $unit",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = if (isSelected) 20.sp else 16.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TimePickerDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Parse existing time (could be in 24h or 12h format)
    val timeParts = value.replace(Regex("[APMapm\\s]"), "").split(":")
    val initialHour = timeParts.getOrNull(0)?.toIntOrNull() ?: 7
    val initialMinute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

    // Convert to 12-hour format
    val initial12Hour = if (initialHour == 0) 12 else if (initialHour > 12) initialHour - 12 else initialHour
    val initialAmPm = if (initialHour >= 12) "PM" else "AM"

    var selectedHour by remember { mutableIntStateOf(initial12Hour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedAmPm by remember { mutableStateOf(initialAmPm) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    TextButton(onClick = {
                        // Convert back to 24-hour format for storage
                        val hour24 = when {
                            selectedAmPm == "AM" && selectedHour == 12 -> 0
                            selectedAmPm == "PM" && selectedHour != 12 -> selectedHour + 12
                            else -> selectedHour
                        }
                        onValueChange(
                            "${hour24.toString().padStart(2, '0')}:${
                                selectedMinute.toString().padStart(2, '0')
                            } $selectedAmPm"
                        )
                        onDismiss()
                    }) {
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Hour Picker (1-12)
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Hour",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor1
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val hourListState = rememberLazyListState(
                            initialFirstVisibleItemIndex = (selectedHour - 1).coerceIn(0, 11)
                        )

                        LazyColumn(
                            state = hourListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items((1..12).toList()) { hour ->
                                val isSelected = hour == selectedHour
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                    )
                                                )
                                            } else {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Transparent
                                                    )
                                                )
                                            }
                                        )
                                        .clickable { selectedHour = hour }
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        text = hour.toString().padStart(2, '0'),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = if (isSelected) 18.sp else 16.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    // Minute Picker
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Minute",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val minuteListState = rememberLazyListState(
                            initialFirstVisibleItemIndex = selectedMinute.coerceIn(0, 59)
                        )

                        LazyColumn(
                            state = minuteListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items((0..59).toList()) { minute ->
                                val isSelected = minute == selectedMinute
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                    )
                                                )
                                            } else {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Transparent
                                                    )
                                                )
                                            }
                                        )
                                        .clickable { selectedMinute = minute }
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(
                                        text = minute.toString().padStart(2, '0'),
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = if (isSelected) 18.sp else 16.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    // AM/PM Picker
                    Column(
                        modifier = Modifier.weight(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Period",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            listOf("AM", "PM").forEach { period ->
                                val isSelected = period == selectedAmPm
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                                    )
                                                )
                                            } else {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Transparent
                                                    )
                                                )
                                            }
                                        )
                                        .clickable { selectedAmPm = period }
                                        .padding(vertical = 16.dp)
                                ) {
                                    Text(
                                        text = period,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = if (isSelected) 18.sp else 16.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        ),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimePickerField(
    label: String,
    value: String,
    onClick: () -> Unit,
    color: Color
) {
    val displayTime = if (value.isNotEmpty()) {
        val parts = value.replace(Regex("[APMapm\\s]"), "").split(":")
        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val amPm = if (hour >= 12) "PM" else "AM"

        "${hour12.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $amPm"
    } else {
        "00:00 AM"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color.copy(alpha = 0.05f))
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Text(
                text = displayTime,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (value.isEmpty()) MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f) else color
                )
            )
        }
    }
}

@Composable
private fun AvatarSelectionCard(
    selectedGender: String,
    onGenderSelect: (String) -> Unit
) {
    EditCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Avatar",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AvatarOption(
                    gender = "Male",
                    isSelected = selectedGender.equals("Male", ignoreCase = true),
                    onClick = { onGenderSelect("Male") }
                )

                AvatarOption(
                    gender = "Female",
                    isSelected = selectedGender.equals("Female", ignoreCase = true),
                    onClick = { onGenderSelect("Female") }
                )
            }
        }
    }
}

@Composable
private fun AvatarOption(
    gender: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Canvas(modifier = Modifier.size(120.dp)) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF00C9FF),
                                Color(0xFF0090FF)
                            )
                        ),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected)             {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF00C9FF).copy(alpha = 0.1f),
                                    Color(0xFF0090FF).copy(alpha = 0.1f)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFF5F5F5),
                                    Color(0xFFF5F5F5)
                                )
                            )
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color.Transparent else Color(0xFFE0E0E0),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale),
                    contentDescription = gender,
                    painter = painterResource(
                        if (gender.equals("Male", ignoreCase = true)) R.drawable.image_man
                        else R.drawable.image_woman
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = gender,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) primary else MaterialTheme.colorScheme.secondary
            )
        )

        if (isSelected) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(primary)
            )
        }
    }
}

@Composable
private fun EditCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            primary.copy(alpha = 0.2f),
                            primary.copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
private fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = primary,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}


private fun Modifier.graphicsLayer(
    scaleX: Float = 1f,
    scaleY: Float = 1f
) = this.then(
    Modifier.scale(scaleX, scaleY)
)
