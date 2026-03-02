// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    // ↑ KSP = Kotlin Symbol Processing. Reemplazo moderno de kapt.
    // Hilt lo usa para procesar las anotaciones (@Inject, @Module, etc.).
    id("com.google.dagger.hilt.android") version "2.56" apply false
    alias(libs.plugins.kotlin.android) apply false
    // ↑ Plugin de Hilt. Genera código en tiempo de compilación.

}