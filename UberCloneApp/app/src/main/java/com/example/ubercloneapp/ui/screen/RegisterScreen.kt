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
fun RegisterScreen(
    authVm:       AuthViewModel,
    onRegisterOk: () -> Unit,
    onGoLogin:    () -> Unit
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm  by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }
    // ↑ Error local (las contraseñas no coinciden) vs error de Firebase.

    val state = authVm.authState
    val context = LocalContext.current  // ← NUEVO: para Google Sign-In

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onRegisterOk()
    }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📝 Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(32.dp))

        // ═══════════════════════════════════════════
        //  BOTÓN DE GOOGLE SIGN-IN  ← NUEVO
        // ═══════════════════════════════════════════
        // Con Google, registro y login son lo mismo:
        // si el usuario es nuevo, Firebase lo crea;
        // si ya existe, simplemente inicia sesión.
        OutlinedButton(
            onClick = { authVm.signInWithGoogle(context) },
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("🔵 Registrarse con Google")
        }

        Spacer(Modifier.height(16.dp))

        // ── Separador ──
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(Modifier.weight(1f))
            Text("  o  ", color = MaterialTheme.colorScheme.onSurfaceVariant)
            HorizontalDivider(Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // ── Formulario email/contraseña (como antes) ──
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña (mín. 6 caracteres)") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Repetir contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        // Mostrar errores (local o de Firebase)
        if (localError.isNotBlank()) {
            Text(localError, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }
        if (state is AuthState.Error) {
            Text(state.msg, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                localError = ""  // Limpiar error anterior
                when {
                    password.length < 6 ->
                        localError = "La contraseña debe tener al menos 6 caracteres"
                    password != confirm ->
                        localError = "Las contraseñas no coinciden"
                    else ->
                        authVm.register(email.trim(), password)
                }
            },
            enabled = state !is AuthState.Loading
                    && email.isNotBlank()
                    && password.isNotBlank()
                    && confirm.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Crear Cuenta") }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onGoLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}