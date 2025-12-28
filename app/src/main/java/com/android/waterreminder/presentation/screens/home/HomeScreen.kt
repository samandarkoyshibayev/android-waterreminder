package com.android.waterreminder.presentation.screens.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.theme.extendedColors
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.format.DateTimeFormatter


class HomeScreen : Screen {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        val viewmodel: HomeScreenContract.ViewModel = getViewModel<HomeScreenViewmodel>()
        val uiState = viewmodel.collectAsState().value
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewmodel.onEventDispatcher(HomeScreenContract.Intent.LoadData)
        }

        viewmodel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is HomeScreenContract.SideEffect.ShowMessage -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        HomeScreenContent(
            uiState = uiState,
            onAddDrink = { amount ->
                viewmodel.onEventDispatcher(HomeScreenContract.Intent.AddDrink(amount))
            },
            showReminder = {
                viewmodel.onEventDispatcher(HomeScreenContract.Intent.ShowReminder)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeScreenContent(
    uiState: HomeScreenContract.UIState,
    onAddDrink: (Int) -> Unit,
    showReminder: () -> Unit
) {
    var selectedAmount by remember { mutableIntStateOf(200) }
    Scaffold(
        topBar = {
            TopBar(uiState, showReminder)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            CircularWaterProgress(
                drunkWaterMl = uiState.drunkWaterMl,
                totalWaterMl = uiState.totalWaterMl,
                progressPercent = uiState.progressPercent
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LastDrinkCard(
                    modifier = Modifier.weight(1f),
                    uiState = uiState
                )

                TargetCard(
                    modifier = Modifier.weight(1f),
                    targetMl = uiState.totalWaterMl
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Choose Amount",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            WaterAmountChooser(
                selectedAmount = selectedAmount,
                onAmountSelected = { selectedAmount = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (uiState.canAddDrink) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.extendedColors.extendedColor1,
                                    MaterialTheme.extendedColors.extendedColor2
                                )
                            )
                        }
                    )
                    .clickable(enabled = uiState.canAddDrink) {
                        onAddDrink(selectedAmount)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cup),
                        contentDescription = null,
                        tint = MaterialTheme.extendedColors.white,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.isGoalReached) "Goal Reached!" else "Add $selectedAmount ml",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColors.white
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CircularWaterProgress(
    drunkWaterMl: Int,
    totalWaterMl: Int,
    progressPercent: Int
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progressPercent) {
        animatedProgress.animateTo(
            targetValue = progressPercent / 100f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        val surfaceColor = MaterialTheme.extendedColors.extendedColor3

        Canvas(modifier = Modifier.size(220.dp)) {

            drawCircle(
                color = surfaceColor,
                radius = size.minDimension / 2,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF4FC3F7),
                        Color(0xFF29B6F6),
                        Color(0xFF03A9F4)
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress.value,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_drop_water),
                contentDescription = null,
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$drunkWaterMl ml",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = "of $totalWaterMl ml",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$progressPercent%",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun LastDrinkCard(
    modifier: Modifier = Modifier,
    uiState: HomeScreenContract.UIState
) {
    val formatter = DateTimeFormatter.ofPattern("h:mm a")

    Column(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColors.extendedColor4),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cup),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Last Drink",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary

                )
            )
        }

        Column {
            Text(
                text = "${uiState.lastDrinkAmount} ml",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = uiState.lastDrinkTime?.format(formatter) ?: "No data",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary

                )
            )
        }
    }
}

@Composable
private fun TargetCard(
    modifier: Modifier = Modifier,
    targetMl: Int
) {
    Column(
        modifier = modifier
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.extendedColors.extendedColor5),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_goal),
                    contentDescription = null,
                    tint = MaterialTheme.extendedColors.orange,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Column {
            Text(
                text = "$targetMl ml",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = "Target",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
private fun WaterAmountChooser(
    selectedAmount: Int,
    onAmountSelected: (Int) -> Unit
) {
    val amounts = listOf(200, 300, 400, 500)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        amounts.forEach { amount ->
            val isSelected = selectedAmount == amount
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(70.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    )
                    .clickable { onAmountSelected(amount) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_cup),
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.extendedColors.white else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$amount ml",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.extendedColors.white else MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    uiState: HomeScreenContract.UIState,
    showReminder: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(vertical = 24.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Good Morning,",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uiState.fullName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    showReminder.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ring),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}