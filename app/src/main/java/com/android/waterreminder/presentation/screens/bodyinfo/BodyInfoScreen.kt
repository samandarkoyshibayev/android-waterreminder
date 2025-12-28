package com.android.waterreminder.presentation.screens.bodyinfo

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.theme.extendedColors
import java.util.Calendar
import kotlin.math.roundToInt

class BodyInfoScreen : Screen {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val viewmodel = getViewModel<BodyInfoScreenViewModel>()

        BodyInfoScreenContent(
            onFinish = { gender, age, weight, height, wakeUpTime, sleepTime ->
                viewmodel.saveBodyInfo(gender, age, weight, height, wakeUpTime, sleepTime)
            }
        )
    }
}

@Composable
private fun BodyInfoScreenContent(
    onFinish: (String, Int, Int, Int, String, String) -> Unit
) {
    var selectedGender by remember { mutableStateOf("male") }
    var age by remember { mutableFloatStateOf(33f) }
    var weight by remember { mutableFloatStateOf(70f) }
    var height by remember { mutableFloatStateOf(170f) }
    var wakeUpTime by remember { mutableStateOf("Select Time") }
    var sleepTime by remember { mutableStateOf("Select Time") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.onBackground)
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 11.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(20.dp),
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Select your age, weight,\n gender and height",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 22.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "What is your gender",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .padding(top = 9.dp)
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                selectedGender = "male"
                            }
                    ) {
                        Image(
                            painter = painterResource(
                                if (selectedGender == "male")
                                    R.drawable.ic_selected
                                else
                                    R.drawable.ic_not_selected
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_man),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                selectedGender = "female"
                            }
                    ) {
                        Image(
                            painter = painterResource(
                                if (selectedGender == "female")
                                    R.drawable.ic_selected
                                else
                                    R.drawable.ic_not_selected
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_woman),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp),
                            tint = MaterialTheme.extendedColors.womanColor
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(75.dp)
                ) {
                    Text(
                        text = "How old are you",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    CustomSlider(
                        value = age,
                        onValueChange = { age = it },
                        valueRange = 0f..100f,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(75.dp)
                ) {
                    Text(
                        text = "What is your weight (in kg)",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    CustomSlider(
                        value = weight,
                        onValueChange = { weight = it },
                        valueRange = 0f..200f,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(75.dp)
                ) {
                    Text(
                        text = "What is your height (in cm)",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    CustomSlider(
                        value = height,
                        onValueChange = { height = it },
                        valueRange = 0f..200f,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(85.dp)
                ) {
                    Text(
                        text = "Wake up time",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    TimePickerButton(
                        time = wakeUpTime,
                        onTimeSelected = { wakeUpTime = it }
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(85.dp)
                ) {
                    Text(
                        text = "Sleeping time",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    TimePickerButton(
                        time = sleepTime,
                        onTimeSelected = { sleepTime = it }
                    )
                }

                Spacer(modifier = Modifier.weight(2f))

                Button(
                    onClick = {
                        onFinish(
                            selectedGender,
                            age.roundToInt(),
                            weight.roundToInt(),
                            height.roundToInt(),
                            wakeUpTime,
                            sleepTime
                        )
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Finish",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.extendedColors.white,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    modifier: Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    thumbColor: Color = MaterialTheme.colorScheme.surface
) {
    var sliderWidth by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    val thumbRadius = with(density) { 28.dp.toPx() }
    val trackHeight = with(density) { 6.dp.toPx() }

    fun calculateValueFromPosition(position: Float): Float {
        val coercedPosition = position.coerceIn(0f, sliderWidth)
        val normalized = if (sliderWidth > 0) coercedPosition / sliderWidth else 0f
        return (valueRange.start + normalized * (valueRange.endInclusive - valueRange.start))
            .coerceIn(valueRange.start, valueRange.endInclusive)
    }

    val targetThumbPosition =
        ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * sliderWidth

    val animatedThumbPosition by animateFloatAsState(
        targetValue = targetThumbPosition,
        animationSpec = tween(durationMillis = 80),
        label = "thumbAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .onGloballyPositioned { coordinates ->
                sliderWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newValue = calculateValueFromPosition(offset.x)
                    onValueChange(newValue)
                }
            }
            .pointerInput(Unit) {
                var currentPosition = animatedThumbPosition
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        currentPosition = animatedThumbPosition
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        currentPosition = (currentPosition + dragAmount).coerceIn(0f, sliderWidth)
                        val newValue = calculateValueFromPosition(currentPosition)
                        onValueChange(newValue)
                    }
                )
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.Center)
        ) {
            val trackY = size.height / 2

            drawLine(
                color = inactiveColor,
                start = Offset(0f, trackY),
                end = Offset(size.width, trackY),
                strokeWidth = trackHeight,
                cap = StrokeCap.Round
            )

            drawLine(
                color = activeColor,
                start = Offset(0f, trackY),
                end = Offset(animatedThumbPosition, trackY),
                strokeWidth = trackHeight,
                cap = StrokeCap.Round
            )
        }

        Box(
            modifier = Modifier
                .offset(
                    x = with(density) { (animatedThumbPosition - 14.dp.toPx()).toDp() },
                )
                .align(Alignment.CenterStart)
                .size(28.dp)
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = thumbColor,
                shadowElevation = 3.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = value.roundToInt().toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Text(
            text = valueRange.start.roundToInt().toString(),
            modifier = Modifier
                .align(Alignment.BottomStart),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        )

        Text(
            text = valueRange.endInclusive.roundToInt().toString(),
            modifier = Modifier
                .align(Alignment.BottomEnd),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun TimePickerButton(
    time: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            val amPm = if (selectedHour >= 12) "PM" else "AM"
            val hourIn12 = if (selectedHour % 12 == 0) 12 else selectedHour % 12
            val selectedTime = String.format("%02d:%02d %s", hourIn12, selectedMinute, amPm)
            onTimeSelected(selectedTime)
        },
        hour,
        minute,
        false
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { timePickerDialog.show() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = time,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}