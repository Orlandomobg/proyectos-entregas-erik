import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.ubercloneapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ubercloneapp"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Leer la API Key de local.properties y pasarla al Manifest
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
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    // ↑ viewModel() en Compose. Crea/recupera ViewModels.

    implementation("androidx.navigation:navigation-compose:2.7.7")
    // ↑ Navegación entre pantallas en Compose.

    // ── Firebase (BOM + Auth + Firestore) ──
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    // ── Google Maps para Compose ──
    implementation("com.google.maps.android:maps-compose:4.3.3")
    // ↑ Componente GoogleMap() para Jetpack Compose.
    // Sin esto, tendrías que usar el MapView clásico (XML).

    implementation("com.google.android.gms:play-services-maps:19.0.0")
    // ↑ El SDK base de Google Maps. maps-compose lo usa internamente.

    implementation("com.google.android.gms:play-services-location:21.3.0")
    // ↑ FusedLocationProviderClient: la forma moderna de obtener
    // la ubicación GPS. Más preciso y eficiente que el LocationManager clásico.

    // ── Permisos en Compose ──
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    // ↑ Librería de Google para pedir permisos de forma declarativa
    // en Compose. Sin esto, tendrías que usar el sistema clásico
    // de ActivityResultContracts (mucho más código).

    // ── Retrofit (para la Directions API — rutas) ──
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // ↑ Usaremos Retrofit para llamar a la Directions API de Google
    // y obtener la polyline (ruta) entre origen y destino.

    // ── Stripe (pagos) ──
    implementation("com.stripe:stripe-android:22.6.0")
    // ↑ SDK oficial de Stripe para Android.
    // Incluye PaymentSheet: una UI preconfigurada para cobrar.
    // También incluye clases para Compose (rememberPaymentSheet, etc.).
    // Repo: github.com/stripe/stripe-android

    // ── Google Sign-In (Credential Manager) ──
    implementation("androidx.credentials:credentials:1.3.0")
    // ↑ API unificada de Android para manejar credenciales
    // (contraseñas, passkeys, tokens de Google, etc.).
    // Reemplaza al antiguo GoogleSignInClient (deprecado).

    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    // ↑ Conecta Credential Manager con Google Play Services.
    // Es el "puente" que hace que Credential Manager pueda
    // mostrar el selector de cuentas de Google en el dispositivo.

    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    // ↑ Clases específicas para Google ID tokens.
    // Proporciona GetGoogleIdOption y GoogleIdTokenCredential
    // que usamos para construir la petición de login con Google.

    // ── Hilt ──
    implementation("com.google.dagger:hilt-android:2.56")
    ksp("com.google.dagger:hilt-compiler:2.56")
    // ↑ Procesador de anotaciones. Genera el código de inyección.
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // ↑ hiltViewModel() — obtener ViewModels inyectados en Compose.

    implementation("com.google.firebase:firebase-messaging-ktx")
// ↑ Firebase Cloud Messaging. La versión la gestiona el BOM.
// Incluye el servicio que recibe mensajes en background.

    implementation("com.google.firebase:firebase-storage-ktx")
// ↑ Firebase Storage — almacenamiento de archivos (imágenes, PDFs, etc.).

    implementation("io.coil-kt:coil-compose:2.6.0")
}