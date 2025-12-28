package com.android.waterreminder.presentation.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.android.waterreminder.R
import com.android.waterreminder.ui.theme.WaterReminderTheme
import com.android.waterreminder.ui.theme.extendedColors
import com.android.waterreminder.ui.theme.textColor1
import com.android.waterreminder.ui.theme.white
import org.orbitmvi.orbit.compose.collectAsState

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: ProfileScreenContract.ViewModel = getViewModel<ProfileScreenViewModel>()
        val uiState = viewModel.collectAsState().value

        ProfileScreenContent(
            uiState = uiState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileScreenContract.UIState,
    onEventDispatcher: (ProfileScreenContract.Intent) -> Unit
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->

        val fullName = "${uiState.firstname} ${uiState.lastname}"

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.my_profile),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Brush.linearGradient(listOf(MaterialTheme.extendedColors.backgroundCardColor1, MaterialTheme.extendedColors.backgroundCardColor2)),
                            CircleShape
                        )
                        .clickable { onEventDispatcher(ProfileScreenContract.Intent.OnEditClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_pen),
                        contentDescription = "Edit",
                        tint = MaterialTheme.extendedColors.white
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(listOf(MaterialTheme.extendedColors.backgroundCardColor1, MaterialTheme.extendedColors.backgroundCardColor2))
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (fullName.length > 10) fullName.take(10) + "..." else fullName,
                            color = MaterialTheme.extendedColors.white,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.extendedColors.white.copy(alpha = 0.25f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${uiState.age} ${stringResource(R.string.years_old)}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.extendedColors.white
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.extendedColors.white.copy(alpha = 0.25f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                val genderText = when (uiState.gender.lowercase()) {
                                    "male" -> stringResource(R.string.male)
                                    "female" -> stringResource(R.string.female)
                                    else -> stringResource(R.string.unknown)
                                }
                                Text(
                                    text = genderText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.extendedColors.white
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Image(
                        painter = painterResource(
                            if (uiState.gender.lowercase() == "male") R.drawable.image_man
                            else R.drawable.image_woman
                        ),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.extendedColors.white.copy(alpha = 0.2f))
                            .padding(8.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.body_metrics),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.weight),
                    value = "${uiState.weight}",
                    unit = stringResource(R.string.weight_format),
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.height),
                    value = "${uiState.height}",
                    unit = stringResource(R.string.height_format),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.daily_routine),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = stringResource(R.string.wake_up_time),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(
                            color = MaterialTheme.extendedColors.orangeColor.copy(0.05f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = uiState.wakeUptime,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColors.orangeColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.sleep_time),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .background(
                            color = MaterialTheme.extendedColors.pinkColor.copy(0.05f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = uiState.sleepTime,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColors.pinkColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    unit: String,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "$value $unit",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ProfileScreenContentPreview() {
    WaterReminderTheme {
        ProfileScreenContent(
            uiState = ProfileScreenContract.UIState(), {}
        )
    }
}
