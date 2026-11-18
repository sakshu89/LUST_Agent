plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "ai.lust"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.lust.assistant"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "LUST_CLOUD_API_KEY",
            "\"" + (providers.gradleProperty("LUST_CLOUD_API_KEY").orNull
                ?: System.getenv("LUST_CLOUD_API_KEY") ?: "") + "\""
        )
        buildConfigField(
            "String",
            "LUST_CLOUD_ENDPOINT",
            "\"" + (providers.gradleProperty("LUST_CLOUD_ENDPOINT").orNull
                ?: System.getenv("LUST_CLOUD_ENDPOINT") ?: "") + "\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))

    // Keep the original multi-module project dependencies.
    implementation(project(":core:domain"))
    implementation(project(":core:providers"))
    implementation(project(":providers:mock"))
    implementation(project(":modules:tools"))

    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
}
