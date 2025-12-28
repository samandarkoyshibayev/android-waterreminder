package com.android.waterreminder.presentation.screens.reminder_mode

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.android.waterreminder.ui.theme.WaterReminderTheme
import com.android.waterreminder.ui.theme.extendedColors
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class ReminderModeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: ReminderModeViewModel = getViewModel()
        val uiState = viewModel.collectAsState().value
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.onEventDispatcher(ReminderModeContract.Intent.LoadCurrentInterval)
        }

        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is ReminderModeContract.SideEffect.ShowMessage -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }

            }
        }

        WaterReminderTheme {
            ReminderModeContent(
                uiState = uiState,
                onIntervalSelected = { interval ->
                    viewModel.onEventDispatcher(ReminderModeContract.Intent.SelectInterval(interval))
                },
                onSave = {
                    viewModel.onEventDispatcher(ReminderModeContract.Intent.SaveInterval)
                },
                onBack = {
                    viewModel.onEventDispatcher(ReminderModeContract.Intent.NavigateBack)
                }
            )
        }
    }
}

@Composable
private fun ReminderModeContent(
    uiState: ReminderModeContract.UIState,
    onIntervalSelected: (Int) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 11.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                Text(
                    text = "Reminder Interval", style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
                )

                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(20.dp)
                        .clickable {
                            onBack.invoke()
                        },
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            Text(
                text = "How often would you like to be reminded?",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose the interval between reminders (15-240 minutes)",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            val intervals = listOf(
                15, 30, 45, 60,
                90, 120, 150, 180,
                210, 240
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(intervals) { interval ->
                    IntervalCard(
                        interval = interval,
                        isSelected = uiState.selectedInterval == interval,
                        onClick = { onIntervalSelected(interval) }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(2f))
            Box(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer)
                        )
                    )
                    .clickable { onSave() },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "Save Changes",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.extendedColors.white
                    )
                )
            }
        }
    }
}

@Composable
private fun IntervalCard(
    interval: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primaryContainer)
                    )
                } else {
                    Brush.verticalGradient(colors = listOf(MaterialTheme.colorScheme.surface,MaterialTheme.colorScheme.surface))
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$interval",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.extendedColors.white else MaterialTheme.colorScheme.onPrimary
                )
            )
            Text(
                text = "minutes",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.extendedColors.white.copy(alpha = 0.9f) else MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ReminderModeContentPreview() {
    WaterReminderTheme {
        val sampleState = ReminderModeContract.UIState(
            selectedInterval = 60
        )
        ReminderModeContent(
            sampleState,
            onIntervalSelected = {},
            onSave = {},
            onBack = {})
    }
}
