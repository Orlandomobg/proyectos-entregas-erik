package com.example.ubercloneapp.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ═══════════════════════════════════════════
//  ESQUEMAS DE COLOR: CLARO Y OSCURO
// ═══════════════════════════════════════════
// Material 3 necesita un ColorScheme completo.
// Cada propiedad (primary, surface, error...) se usa
// automáticamente por los componentes de Material 3.

private val LightColors = lightColorScheme(
    primary          = UberGreen,
    // ↑ Color principal. Usado por Button, FAB, etc.
    onPrimary        = Color.White,
    // ↑ Color del TEXTO sobre primary. Blanco sobre azul.
    primaryContainer = UberGreen.copy(alpha = 0.12f),
    // ↑ Versión suave del primary. Fondos de chips, badges.
    surface          = SurfaceLight,
    // ↑ Color de fondo de tarjetas, diálogos, sheets.
    background       = Color.White,
    error            = ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary          = UberGreen,
    onPrimary        = Color.White,
    primaryContainer = UberGreenDark,
    surface          = SurfaceDark,
    background       = Color(0xFF121212),
    // ↑ Fondo oscuro estándar de Material Design.
    onSurface        = OnSurfaceDark,
    onBackground     = OnSurfaceDark,
    error            = ErrorRed,
)

// ═══════════════════════════════════════════
//  COMPOSABLE DEL TEMA
// ═══════════════════════════════════════════

@Composable
fun UberCloneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ↑ Por defecto, sigue la configuración del sistema.
    // Si el usuario tiene el modo oscuro activado → true.
    dynamicColor: Boolean = true,
    // ↑ Si true Y Android 12+, usa colores del wallpaper.
    content: @Composable () -> Unit
) {
    // ① Decidir qué esquema de colores usar
    val colorScheme = when {
        // Caso 1: Android 12+ con colores dinámicos activados
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
            // ↑ Extrae colores del fondo de pantalla del usuario.
            // Cada dispositivo tendrá colores únicos.
        }
        // Caso 2: Android < 12 o dinámico desactivado
        darkTheme -> DarkColors
        else -> LightColors
    }

    // ② Aplicar el tema a todo el contenido
    MaterialTheme(
        colorScheme = colorScheme,
        // ↑ Todos los composables hijos (Button, Text, Card...)
        // leerán estos colores automáticamente.
        content = content
    )
}