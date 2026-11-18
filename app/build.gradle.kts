plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}
<<<<<<< HEAD

=======
>>>>>>> origin/main
android {
    namespace = "ai.lust"
    compileSdk = 35
    defaultConfig {
        applicationId = "ai.lust.assistant"
<<<<<<< HEAD
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
=======
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
    buildFeatures { compose = true }
>>>>>>> origin/main
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
<<<<<<< HEAD
    buildFeatures {
        compose = true
        buildConfig = true
    }

    // Supply LUST_CLOUD_API_KEY and LUST_CLOUD_ENDPOINT as CI secrets/Gradle properties.
    // Never commit the real key to source control.
    defaultConfig {
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
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
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
=======
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:providers"))
    implementation(project(":providers:mock"))
    implementation(project(":modules:tools"))
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
>>>>>>> origin/main
}
