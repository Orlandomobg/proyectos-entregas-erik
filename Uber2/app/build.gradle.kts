import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.uber"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.uber"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val props = Properties()
        props.load(rootProject.file("local.properties").inputStream())
        manifestPlaceholders["MAPS_API_KEY"] = props.getProperty("MAPS_API_KEY", "")
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
    implementation(libs.compose.icons.extended)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // nuevo
    implementation("androidx.navigation:navigation-compose:2.8.5")
    //viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson convierte json a kotlin
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // coil -- cargar imagenes desde url
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    // Necesario para usar .await() en los ViewModels (Corrutinas)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.35")

    //splashscreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // status bar dinamica
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    // ── Google Sign-In (Credential Manager) ──
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    //faltantes
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //maps
    implementation("com.google.maps.android:maps-compose:4.3.3")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    //permisos compose
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    //stripe
    implementation("com.stripe:stripe-android:22.6.0")

    //hilt
    // ── Hilt ──
    implementation("com.google.dagger:hilt-android:2.56")
    ksp("com.google.dagger:hilt-compiler:2.56")
    // ↑ Procesador de anotaciones. Genera el código de inyección.
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // ↑ hiltViewModel() — obtener ViewModels inyectados en Compose.

    //room
    implementation("androidx.room:room-runtime:2.6.1")
// ↑ Librería principal de Room.
    implementation("androidx.room:room-ktx:2.6.1")
// ↑ Extensiones Kotlin: soporte para coroutines (suspend fun).
    ksp("androidx.room:room-compiler:2.6.1")
// ↑ Procesador que genera el código SQL a partir de tus anotaciones.

}
