package com.android.waterreminder.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.android.waterreminder.ui.theme.primary
import com.android.waterreminder.ui.theme.white

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        WaterReminderTheme {
            val viewModel: SplashScreenViewmodel = getViewModel()
            SplashScreenContent()
        }
    }
}

@Composable
private fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = primary)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .size(258.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.image_drops_white),
                modifier = Modifier
                    .width(151.dp)
                    .height(171.dp),
                contentDescription = null
            )

            Text(
                text = "Drops Water Tracker",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    color = white,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Stay hydrated and track your\n daily water intake",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = white,
                    fontWeight = FontWeight.Medium
                )
            )


        }

        Image(
            painter = painterResource(R.drawable.image_wave2),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentDescription = null
        )

        Image(
            painter = painterResource(R.drawable.image_wave1),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            contentDescription = null
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SplashScreenContentPreview() {
    WaterReminderTheme {
        SplashScreenContent()
    }
}