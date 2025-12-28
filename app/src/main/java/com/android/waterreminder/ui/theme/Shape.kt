package com.android.waterreminder.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object AppShapes {

    // --- Basic Corner Radii ---
    val None = RoundedCornerShape(0.dp)
    val XXSmall = RoundedCornerShape(2.dp)
    val XSmall = RoundedCornerShape(4.dp)
    val Small = RoundedCornerShape(6.dp)
    val Medium = RoundedCornerShape(8.dp)
    val XMedium = RoundedCornerShape(10.dp)
    val Large = RoundedCornerShape(12.dp)
    val XLarge = RoundedCornerShape(16.dp)
    val XXLarge = RoundedCornerShape(20.dp)
    val XXXLarge = RoundedCornerShape(24.dp)
    val Huge = RoundedCornerShape(28.dp)
    val XHuge = RoundedCornerShape(32.dp)
    val XXHuge = RoundedCornerShape(40.dp)
    val XXXHuge = RoundedCornerShape(48.dp)
    val XXXXLarge = RoundedCornerShape(56.dp)
    val Massive = RoundedCornerShape(64.dp)

    // --- Special One-Sided Shapes ---
    val TopSmall = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
    val TopMedium = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    val TopLarge = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    val TopXLarge = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

    val BottomSmall = RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp)
    val BottomMedium = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
    val BottomLarge = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
    val BottomXLarge = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)

    val StartSmall = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
    val StartMedium = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
    val StartLarge = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)

    val EndSmall = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
    val EndMedium = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
    val EndLarge = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)

    // --- Fully Rounded / Circular Shapes ---
    val Pill = RoundedCornerShape(50)   // For buttons, chips
    val Circle = RoundedCornerShape(100) // For avatars, icons, etc.
    val Full = RoundedCornerShape(50)    // Alias for full rounding
}
