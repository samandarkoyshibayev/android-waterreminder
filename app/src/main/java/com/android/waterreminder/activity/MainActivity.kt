package com.android.waterreminder.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.presentation.screens.splash.SplashScreen
import com.android.waterreminder.ui.navigation.NavigationHandler
import com.android.waterreminder.ui.theme.WaterReminderTheme
import com.android.waterreminder.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), SensorEventListener {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    @Inject
    lateinit var appPreferenceHelper: AppPreferenceHelper

    private var totalSteps: Float = 0f
    private var previousTotalSteps: Float = 0f

    private val themeFlow = MutableStateFlow("system")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launchWhenStarted {
            appPreferenceHelper.getThemeMode().collect { theme ->
                themeFlow.value = theme
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 2)
            }
        }

        setContent {
            val themeMode = themeFlow.collectAsState().value
            WaterReminderTheme(themeMode = themeMode) {
                Navigator(screen = SplashScreen()) { navigator ->
                    LaunchedEffect(navigator) {
                        navigationHandler.screenState.collect { it(navigator) }
                    }
                    SlideTransition(navigator)
                }
            }
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.d("SensorStep", "❌ Step Counter Sensor not available on this device")
        } else {
            Log.d("SensorStep", "✅ Step Counter Sensor found, listening for steps...")
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val stepsSinceReboot = event.values[0]
            if (previousTotalSteps == 0f) {
                previousTotalSteps = stepsSinceReboot
            }
            val currentSteps = stepsSinceReboot - previousTotalSteps
            Log.d("SensorStep", "Steps detected: ${currentSteps.toInt()}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
