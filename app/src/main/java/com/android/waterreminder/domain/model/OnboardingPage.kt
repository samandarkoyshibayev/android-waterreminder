package com.android.waterreminder.domain.model

import com.android.waterreminder.R

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

val onboardingPages = listOf(
    OnboardingPage(
        "Track your daily water\n intake with Us.",
        "Achieve your hydration goals with a simple\n tap!",
        R.drawable.image_onboarding1
    ),
    OnboardingPage(
        "Smart Reminders\n Tailored to You",
        "Quick and easy to set your hydration goal &\n then track your daily water intake progress.",
        R.drawable.image_onboarding2
    ),
    OnboardingPage(
        "Easy to Use – Drink, Tap,\n Repeat",
        "Staying hydrated every day is easy with\n Drops Water Tracker.",
        R.drawable.image_onboarding3
    )
)
