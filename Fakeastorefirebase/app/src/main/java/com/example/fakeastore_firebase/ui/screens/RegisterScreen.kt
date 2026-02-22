package com.example.fakeastore_firebase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fakeastore_firebase.viewmodel.AuthState
import com.example.fakeastore_firebase.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authVm:       AuthViewModel,
    onRegisterOk: () -> Unit,     // Registro exitoso → navegar al catálogo
    onGoLogin:    () -> Unit      // Volver al login
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm  by remember { mutableStateOf("") }
    // ↑ Campo extra para confirmar contraseña

    val state = authVm.authState

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

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Repetir contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))

        if (state is AuthState.Error) {
            Text(state.msg, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = {
                if (password != confirm) {
                    // Validación LOCAL antes de llamar a Firebase
                    return@Button
                    // ↑ "return@Button" sale solo del onClick, no de toda la función.
                    // Es necesario porque estamos dentro de una lambda.
                }
                authVm.register(email, password)
            },
            enabled = state !is AuthState.Loading
                    && email.isNotBlank()
                    && password.length >= 6
                    && confirm.isNotBlank(),
            // ↑ El botón solo se activa si hay email, la contraseña
            // tiene al menos 6 caracteres (requisito de Firebase)
            // y el campo confirmar no está vacío.
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Cuenta")
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onGoLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}