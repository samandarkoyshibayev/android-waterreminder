package com.android.waterreminder.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightBackground = Color(0xFFF4F8FB)//
val textColor1 = Color(0xFF90A5B4)
val white = Color(0xFFFFFFFF)//
val black = Color.Black//
val lightGray = Color(0xFFF2F2F2)
val gray = Color(0xFF8E8E8E)
val textFieldColor = Color(0xFFF4F4F4)
val womanColor = Color(0xFFFF9DC6)//
private val cursorColor = Color(0xFF007BFF)


val primary = Color(0xFF5DCCFC)//
private val LightTextPrimary = Color(0xFF141A1E)
private val LightSurfaceVariant = Color(0xFFF4F4F4)
private val LightSurface = Color.White
private val LightPrimary = Color(0xFF5DCCFC)//
private val LightDivider = Color(0xFFF0F0F0)
private val LightPrimaryVariant = Color(0xFF0090FF)


private val DarkBackground = Color(0xFF0A0E12)
private val DarkSurface = Color(0xFF1C2127)
private val DarkSurfaceVariant = Color(0xFF252B33)
private val DarkPrimary = Color(0xFF4FC3F7)
private val DarkPrimaryVariant = Color(0xFF29B6F6)
private val DarkTextPrimary = Color(0xFFE8EAED)
private val DarkTextSecondary = Color(0xFF9AA0A6)
private val DarkDivider = Color(0xFF252B33)
private val LightTextSecondary = Color(0xFF90A5B4)

private val ExtendedLightColor1 = Color(0xFFE0E0E0)
private val ExtendedLightColor2 = Color(0xFFBDBDBD)
private val ExtendedLightColor3 = Color(0xFFE8F5FD)
private val ExtendedLightColor4 = Color(0xFFE3F2FD)
private val ExtendedLightColor5 = Color(0xFFFFF3E0)

private val ExtendedDarkColor1 = Color(0xFF3A3F47)
private val ExtendedDarkColor2 = Color(0xFF2D3238)
private val ExtendedDarkColor3 = Color(0xFF1A2229)
private val ExtendedDarkColor4 = Color(0xFF1E3A47)
private val ExtendedDarkColor5 = Color(0xFF3D2E1F)

private val heartRateLightColor = Color(0xFFE91E63)
private val heartRateDarkColor = Color(0xFFEF5350)
private val heartRateBackgroundLightColor = Color(0xFFFFEBEE)

private val heartRateBackgroundDarkColor = Color(0xFF4A2229)
private val backgroundCardLightColor1 = Color(0xFF00C9FF)

private val backgroundCardLightColor2 = Color(0xFF0090FF)
private val backgroundCardDarkColor1 = Color(0xFF1E4A5F)

private val backgroundCardDarkColor2 = Color(0xFF2A5F7F)
private val orangeBackgroundLightColor = Color(0xFFFFF3E0)
private val orangeBackgroundDarkColor = Color(0xFF3D2E1F)
private val LightOrange = Color(0xFFFF9800)
private val DarkOrange = Color(0xFFFFB74D)
private val pink = Color(0xFF7E57C2)

data class ExtendedColors(
    val womanColor: Color,
    val lightGray: Color,
    val gray: Color,
    val white: Color,
    val orange: Color,
    val black: Color,
    val textFieldColor: Color,
    val cursorColor: Color,
    val extendedColor1: Color,
    val extendedColor2: Color,
    val extendedColor3: Color,
    val extendedColor4: Color,
    val extendedColor5: Color,
    val backgroundCardColor1: Color,
    val backgroundCardColor2: Color,
    val redColor: Color,
    val redBackgroundColor: Color,
    val orangeBackgroundColor: Color,
    val orangeColor: Color,
    val pinkColor : Color,
    )

val LightExtendedColors = ExtendedColors(
    womanColor = womanColor,
    lightGray = lightGray,
    gray = gray,
    white = white,
    black = black,
    textFieldColor = textFieldColor,
    cursorColor = cursorColor,
    extendedColor1 = ExtendedLightColor1,
    extendedColor2 = ExtendedLightColor2,
    extendedColor3 = ExtendedLightColor3,
    extendedColor4 = ExtendedLightColor4,
    orange = LightOrange,
    extendedColor5 = ExtendedLightColor5,
    backgroundCardColor1 = backgroundCardLightColor1,
    backgroundCardColor2 = backgroundCardLightColor2,
    redColor = heartRateLightColor,
    redBackgroundColor = heartRateBackgroundLightColor,
    orangeBackgroundColor = orangeBackgroundLightColor,
    orangeColor = DarkOrange,
    pinkColor = pink,
)

val DarkExtendedColors = ExtendedColors(
    womanColor = womanColor,
    lightGray = lightGray,
    gray = gray,
    white = white,
    black = black,
    textFieldColor = textFieldColor,
    cursorColor = cursorColor,
    extendedColor1 = ExtendedDarkColor1,
    extendedColor2 = ExtendedDarkColor2,
    extendedColor3 = ExtendedDarkColor3,
    extendedColor4 = ExtendedDarkColor4,
    orange = DarkOrange,
    extendedColor5 = ExtendedDarkColor5,
    backgroundCardColor1 = backgroundCardDarkColor1,
    backgroundCardColor2 = backgroundCardDarkColor2,
    redColor = heartRateDarkColor,
    redBackgroundColor = heartRateBackgroundDarkColor,
    orangeBackgroundColor = orangeBackgroundDarkColor,
    orangeColor = DarkOrange,
    pinkColor = pink,
)

internal val LightColorScheme = lightColorScheme(
    onBackground = LightSurface,
    primary = LightPrimary,
    primaryContainer = LightPrimaryVariant,
    onPrimary = LightTextPrimary,
    tertiary = LightDivider,
    background = LightBackground,
    secondary = LightTextSecondary,
    secondaryContainer = LightSurface,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant
)

internal val DarkColorScheme = darkColorScheme(
    onBackground = DarkSurface,
    primary = DarkPrimary,
    primaryContainer = DarkPrimaryVariant,
    onPrimary = DarkTextPrimary,
    tertiary = DarkDivider,
    secondary = DarkTextSecondary,
    secondaryContainer = DarkPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant
)