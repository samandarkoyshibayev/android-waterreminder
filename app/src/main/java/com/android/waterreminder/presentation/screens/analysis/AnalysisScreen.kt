package com.android.waterreminder.presentation.screens.analysis

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.theme.WaterReminderTheme
import com.android.waterreminder.ui.theme.extendedColors
import com.android.waterreminder.ui.theme.primary
import com.android.waterreminder.ui.theme.textColor1
import com.android.waterreminder.ui.theme.white
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.math.sin


class AnalysisScreen : Screen {
    @Composable
    override fun Content() {

        val viewModel: AnalysisScreenContract.VieModel = getViewModel<AnalysisScreenViewModel>()
        val uiState = viewModel.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.onEventDispatcher(AnalysisScreenContract.Intent.LoadData)
        }
        AnalysisScreenContent(
            uiState = uiState
        )
    }
}

@Composable
private fun AnalysisScreenContent(
    uiState: AnalysisScreenContract.UIState
) {
    val navigationBarHeight = 70.dp
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = navigationBarHeight + 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.todays_overview),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.track_health_metrics),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor1
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Water Intake Card (Premium)
            WaterIntakeCard(
                currentMl = uiState.totalDrunkWater,
                targetMl = uiState.targetWater
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Heart Rate & Steps Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeartRateCard(
                    modifier = Modifier.weight(1f),
                    heartRate = uiState.heartRate
                )

                StepsCard(
                    modifier = Modifier.weight(1f),
                    steps = 8542,
                    goal = 10000
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BMI & Calories Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BMICard(
                    modifier = Modifier.weight(1f),
                    bmi = uiState.bmi
                )

                CaloriesCard(
                    modifier = Modifier.weight(1f),
                    burned = 1850
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pass real data to WeeklyActivityCard
            WeeklyActivityCard(
                weeklyData = uiState.weeklyData,
                averageCompletion = uiState.averageCompletion
            )
        }
    }
}

@Composable
private fun WaterIntakeCard(
    currentMl: Int,
    targetMl: Int
) {
    val safeTarget = if (targetMl == 0) 1 else targetMl
    val progress = (currentMl.toFloat() / safeTarget.toFloat()).coerceIn(0f, 1f)
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.extendedColors.backgroundCardColor1,
                        MaterialTheme.extendedColors.backgroundCardColor2,
                    )
                )
            )
    ) {
        val secondaryContainer = MaterialTheme.extendedColors.white

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
        ) {
            val waveHeight = 30f
            val path = Path().apply {
                moveTo(0f, size.height * (1 - animatedProgress.value) + waveHeight)

                var x = 0f
                while (x < size.width) {
                    val y = size.height * (1 - animatedProgress.value) +
                            waveHeight * sin(x / 100f)
                    lineTo(x, y)
                    x += 10f
                }

                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        secondaryContainer.copy(alpha = 0.3f),
                        secondaryContainer.copy(alpha = 0.1f)
                    )
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.image_drop_water),
                        contentDescription = null,
                        tint = white,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.water_intake),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = white.copy(alpha = 0.9f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = (currentMl / 1000f).toString().take(3),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColors.white
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.l_unit),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.extendedColors.white.copy(alpha = 0.9f)
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${stringResource(R.string.of)} ${(targetMl / 1000f)} ${stringResource(R.string.l_unit)} ${stringResource(R.string.today)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.extendedColors.white.copy(alpha = 0.8f)
                    )
                )
            }


            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(80.dp)) {
                    drawCircle(
                        color = secondaryContainer.copy(alpha = 0.3f),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )

                    drawArc(
                        color = secondaryContainer,
                        startAngle = -90f,
                        sweepAngle = 360f * animatedProgress.value,
                        useCenter = false,
                        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.extendedColors.white
                    )
                )
            }
        }
    }
}

@Composable
private fun HeartRateCard(
    modifier: Modifier = Modifier,
    heartRate: Int
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .scale(scale)
                    .background(MaterialTheme.extendedColors.redBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart_rate),
                    contentDescription = null,
                    tint = MaterialTheme.extendedColors.redColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$heartRate",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "bpm",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor1
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.heart_rate),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
private fun StepsCard(
    modifier: Modifier = Modifier,
    steps: Int,
    goal: Int
) {
    val progress = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColors.orangeBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_snickers),
                    contentDescription = null,
                    tint = MaterialTheme.extendedColors.orange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${steps / 1000}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Text(
                    text = ".${(steps % 1000) / 100}K",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.steps),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFFF5F5F5))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress.value)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFFFF9800), Color(0xFFFFB74D))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun BMICard(
    modifier: Modifier = Modifier,
    bmi: Float
) {
    val status = when {
        bmi < 18.5f -> stringResource(R.string.bmi_underweight)
        bmi < 25f -> stringResource(R.string.bmi_normal)
        bmi < 30f -> stringResource(R.string.bmi_overweight)
        else -> stringResource(R.string.bmi_obese)
    }

    val statusColor = when {
        bmi < 18.5f -> Color(0xFF2196F3)
        bmi < 25f -> Color(0xFF4CAF50)
        bmi < 30f -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Column(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bmi),
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = bmi.toString().take(4),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.bmi),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CaloriesCard(
    modifier: Modifier = Modifier,
    burned: Int,
) {

    Column(
        modifier = modifier
            .height(160.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColors.redBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calories),
                    contentDescription = null,
                    tint = MaterialTheme.extendedColors.redColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$burned",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "kcal",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.calories_burned),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
private fun WeeklyActivityCard(
    weeklyData: List<AnalysisScreenContract.DayData>,
    averageCompletion: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.weekly_activity),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Avg: $averageCompletion% completion",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor1
                    )
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = stringResource(R.string.last_7_days),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (weeklyData.isEmpty()) {
            // Show empty state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.image_drop_water),
                        contentDescription = null,
                        tint = textColor1.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_data_yet),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor1.copy(alpha = 0.5f)
                        )
                    )
                    Text(
                        text = stringResource(R.string.start_tracking_water),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = textColor1.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEach { dayData ->
                    WeeklyBarItem(
                        dayData = dayData,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyBarItem(
    dayData: AnalysisScreenContract.DayData,
    modifier: Modifier = Modifier
) {
    val animatedHeight = remember { Animatable(0f) }

    LaunchedEffect(dayData.percentage) {
        animatedHeight.animateTo(
            targetValue = dayData.percentage / 100f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // Tooltip showing ml amount on hover/tap
        if (dayData.waterMl > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (dayData.isToday) primary.copy(alpha = 0.1f)
                        else Color(0xFFF5F5F5)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "${dayData.waterMl / 1000f}L",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (dayData.isToday) primary else textColor1
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Bar
        Box(
            modifier = Modifier
                .width(32.dp)
                .height((animatedHeight.value * 100).dp.coerceAtLeast(8.dp))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    when {
                        dayData.isToday -> Brush.verticalGradient(
                            colors = listOf(primary, Color(0xFF0090FF))
                        )

                        dayData.percentage >= 100 -> Brush.verticalGradient(
                            colors = listOf(Color(0xFF4CAF50), Color(0xFF66BB6A))
                        )

                        dayData.percentage >= 75 -> Brush.verticalGradient(
                            colors = listOf(Color(0xFF2196F3), Color(0xFF42A5F5))
                        )

                        dayData.percentage > 0 -> Brush.verticalGradient(
                            colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                        )

                        else -> Brush.verticalGradient(
                            colors = listOf(Color(0xFFF5F5F5), Color(0xFFEEEEEE))
                        )
                    }
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Day label
        Text(
            text = dayData.day,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 11.sp,
                fontWeight = if (dayData.isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (dayData.isToday) primary else MaterialTheme.colorScheme.secondary
            )
        )

        // Percentage
        if (dayData.percentage > 0) {
            Text(
                text = "${dayData.percentage}%",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor1.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnalysisScreenContentPreview() {
    WaterReminderTheme {
        AnalysisScreenContent(uiState = AnalysisScreenContract.UIState())
    }
}