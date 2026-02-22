package com.example.fakeastore_firebase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fakeastore_firebase.viewmodel.AuthState
import com.example.fakeastore_firebase.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authVm: AuthViewModel,   // ViewModel de autenticación
    onLoginOk:   () -> Unit,      // Callback: login exitoso → navegar
    onGoRegister: () -> Unit      // Callback: ir a pantalla de registro
) {
    // ── Estado LOCAL de esta pantalla ──
    // "remember" = Compose guarda el valor entre recomposiciones.
    // Sin remember, al repintar se resetearía a "".
    // Este estado es SOLO de esta pantalla, no global.
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ── Observar el estado de autenticación ──
    val state = authVm.authState

    // ── Si el estado cambia a Authenticated → navegar ──
    LaunchedEffect(state) {
        // LaunchedEffect se ejecuta cada vez que "state" cambia.
        // Es como un "observador" que reacciona a cambios.
        if (state is AuthState.Authenticated) {
            onLoginOk()
        }
    }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🔐 Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(32.dp))

        // ── Campo de email ──
        OutlinedTextField(
            value = email,
            // ↑ Lo que muestra el campo. Viene de nuestro estado local.
            onValueChange = { email = it },
            // ↑ Cada vez que el usuario escribe una letra,
            // "it" es el texto completo nuevo. Lo guardamos en email.
            // Compose se repinta y el campo muestra el texto actualizado.
            label = { Text("Email") },
            // ↑ Texto flotante que sube al enfocar el campo.
            singleLine = true,
            // ↑ No permite saltos de línea (Enter).
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // ── Campo de contraseña ──
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            // ↑ Muestra ••••• en vez de las letras reales.
            // El valor real sigue en "password" — solo cambia lo visual.
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // ── Mostrar error si lo hay ──
        if (state is AuthState.Error) {
            Text(state.msg, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }

        // ── Botón de login ──
        Button(
            onClick = { authVm.login(email, password) },
            enabled = state !is AuthState.Loading,
            // ↑ Desactivar mientras carga para evitar doble pulsación.
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state is AuthState.Loading)
                CircularProgressIndicator(Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp)
            else
                Text("Iniciar Sesión")
        }

        Spacer(Modifier.height(16.dp))

        // ── Link a registro ──
        TextButton(onClick = onGoRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}