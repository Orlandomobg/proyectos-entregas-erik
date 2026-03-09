package com.example.ubercloneapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
// ↑ OBLIGATORIO. Es el runner que ejecuta tests en Android.
// Sin este import → "Unresolved reference: AndroidJUnit4".
// Viene de la dependencia: androidx.test.ext:junit:1.1.5
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
// ↑ Le dice a JUnit: "usa el runner de Android para estos tests".
// Sin esto, JUnit intenta correrlos como tests JVM normales → crash.
class UberCloneUiTest {

    @get:Rule
    val composeRule = createComposeRule()
    // ↑ Crea un entorno de Compose aislado para tests.
    // NO necesita Activity ni ViewModel — puedes montar
    // cualquier composable directamente con setContent {}.

    // ═══════════════════════════════════════════
    //  TEST 1: Verificar que un botón se muestra
    // ═══════════════════════════════════════════
    @Test
    fun button_is_displayed() {
        // Arrange: montar un composable simple
        composeRule.setContent {
            Button(onClick = {}) {
                Text("Iniciar Sesión")
            }
        }

        // Assert: verificar que está visible
        composeRule
            .onNodeWithText("Iniciar Sesión")
            // ↑ Busca un nodo que contenga ese texto exacto.
            .assertIsDisplayed()
        // ↑ Verifica que está visible en pantalla.
    }

    // ═══════════════════════════════════════════
    //  TEST 2: Escribir en un TextField
    // ═══════════════════════════════════════════
    @Test
    fun can_type_in_text_field() {
        composeRule.setContent {
            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Email") }
            )
        }

        // Act: escribir texto
        composeRule
            .onNodeWithText("Email")
            .performTextInput("usuario@correo.com")
        // ↑ Simula que el usuario escribe con el teclado.

        // Assert: verificar que el texto se escribió
        composeRule
            .onNodeWithText("usuario@correo.com")
            .assertExists()
    }

    // ═══════════════════════════════════════════
    //  TEST 3: Simular clic y verificar resultado
    // ═══════════════════════════════════════════
    @Test
    fun click_changes_state() {
        composeRule.setContent {
            var count by remember { mutableStateOf(0) }
            Column {
                Text("Viajes: $count")
                Button(onClick = { count++ }) {
                    Text("Añadir viaje")
                }
            }
        }

        // Verificar estado inicial
        composeRule
            .onNodeWithText("Viajes: 0")
            .assertExists()

        // Act: pulsar el botón
        composeRule
            .onNodeWithText("Añadir viaje")
            .performClick()
        // ↑ Simula un toque en el botón.

        // Assert: el contador cambió
        composeRule
            .onNodeWithText("Viajes: 1")
            .assertExists()
        // ↑ Compose se recompuso automáticamente tras el clic.
    }

    // ═══════════════════════════════════════════
    //  TEST 4: Verificar que un elemento NO existe
    // ═══════════════════════════════════════════
    @Test
    fun error_message_hidden_initially() {
        composeRule.setContent {
            // Simulamos una pantalla sin error
            val error: String? = null
            Column {
                Text("Bienvenido")
                if (error != null) {
                    Text(error)
                }
            }
        }

        composeRule
            .onNodeWithText("Bienvenido")
            .assertIsDisplayed()

        // Verificar que NO hay mensaje de error
        composeRule
            .onNodeWithText("Error")
            .assertDoesNotExist()
        // ↑ assertDoesNotExist() = verifica que el nodo NO está.
        // Útil para verificar que errores/loading están ocultos.
    }
}