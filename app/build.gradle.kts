plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {

    namespace = "com.android.waterreminder"

    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.android.waterreminder"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Hilt + WorkManager integration (AndroidX)
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-compiler:2.57.1")

    // Voyager Navigator
    implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-beta03")
    implementation("cafe.adriel.voyager:voyager-hilt:1.1.0-beta03")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:1.1.0-beta03")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:1.1.0-beta03")
    implementation("cafe.adriel.voyager:voyager-transitions:1.1.0-beta03")
    implementation("org.orbit-mvi:orbit-core:10.0.0")
    implementation("org.orbit-mvi:orbit-viewmodel:10.0.0")
    implementation("org.orbit-mvi:orbit-compose:10.0.0")

    val work_version = "2.11.0"

    // (Java only)
    implementation("androidx.work:work-runtime:$work_version")
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$work_version")
    // optional - RxJava2 support
    implementation("androidx.work:work-rxjava2:$work_version")
    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:$work_version")
    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:$work_version")
    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:$work_version")
    implementation("androidx.compose.foundation:foundation:1.7.0")

    // room database
    implementation("androidx.room:room-ktx:2.7.2")
    implementation("androidx.room:room-runtime:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")


}