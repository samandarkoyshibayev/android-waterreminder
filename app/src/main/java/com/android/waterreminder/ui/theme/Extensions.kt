package com.android.waterreminder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalExtendedColors.current

val MaterialTheme.spacing: AppSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalAppSpacing.current

val MaterialTheme.appShapes: AppShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalAppShapes.current