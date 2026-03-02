package com.example.ubercloneapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ubercloneapp.viewmodel.AuthState
import com.example.ubercloneapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authVm:      AuthViewModel,
    onLoginOk:   () -> Unit,
    onGoRegister: () -> Unit
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = authVm.authState

    // Obtenemos el Context para pasarlo a signInWithGoogle()
    // LocalContext.current devuelve la Activity actual en Compose.
    val context = LocalContext.current

    // Cuando el estado cambia a Authenticated → navegar
    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onLoginOk()
    }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🚗 UberClone",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold)

        Text("Inicia sesión para continuar",
            color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(Modifier.height(32.dp))

        // ═══════════════════════════════════════════
        //  BOTÓN DE GOOGLE SIGN-IN  ← NUEVO
        // ═══════════════════════════════════════════
        // Lo ponemos ARRIBA para dar prioridad visual.
        // OutlinedButton = botón con borde pero sin fondo relleno.
        // Así se diferencia visualmente del botón de email/contraseña.
        OutlinedButton(
            onClick = { authVm.signInWithGoogle(context) },
            // ↑ Pasamos el context que obtuvimos con LocalContext.current.
            // El ViewModel lo usa para mostrar el selector de cuentas.
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("🔵 Iniciar con Google")
            // ↑ Usamos un emoji como icono simple. En una app real
            // usarías el logo SVG oficial de Google.
        }

        Spacer(Modifier.height(16.dp))

        // ── Separador visual "o" ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(Modifier.weight(1f))
            Text("  o  ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider(Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // ── Campos de email/contraseña (como antes) ──
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        if (state is AuthState.Error) {
            Text(state.msg, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = { authVm.login(email.trim(), password) },
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state is AuthState.Loading)
                CircularProgressIndicator(Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp)
            else
                Text("Iniciar Sesión")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onGoRegister) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}