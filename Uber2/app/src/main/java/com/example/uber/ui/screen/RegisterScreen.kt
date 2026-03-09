package com.example.uber.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.uber.viewmodel.AuthState
import com.example.uber.viewmodel.AuthViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.uber.R

@Composable
fun RegisterScreen(
    authVm:       AuthViewModel,
    onRegisterOk: () -> Unit,
    onGoLogin:    () -> Unit
) {
    var email      by remember { mutableStateOf("") }
    var password   by remember { mutableStateOf("") }
    var confirm    by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    val state   = authVm.authState
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onRegisterOk()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(200.dp))

        // ── Título a la izquierda ──
        Text(
            "Crear Cuenta",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight(500),
                color = Color.Black
            )
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { authVm.signInWithGoogle(context) },
            shape = RectangleShape,
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth()
                .height(50.dp)
                .border(width = 0.7.dp, color = Color(0xFF000000),shape = RoundedCornerShape(size = 2.dp))
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()){

                Text(
                    text = "Continue with Google",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(fontSize = 17.sp,
                        fontWeight = FontWeight(500))
                )

                Image(
                    painter = painterResource(id = R.drawable.gmaillogo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(19.dp)
                        .align(Alignment.CenterStart)
                )

            }
        }

        Spacer(Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(Modifier.weight(1f), color = Color.LightGray)
            Text("  o  ", color = Color.Black)
            HorizontalDivider(Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(Modifier.height(16.dp))

        // ── Campos negros ──
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email", color = Color.LightGray) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña (mín. 6 caracteres)", color = Color.LightGray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Repetir contraseña", color = Color.LightGray) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(Modifier.height(24.dp))

        if (localError.isNotBlank()) {
            Text(localError, color = Color.Red)
            Spacer(Modifier.height(8.dp))
        }
        if (state is AuthState.Error) {
            Text(state.msg, color = Color.Red)
            Spacer(Modifier.height(8.dp))
        }

        // ── Botón crear cuenta gris/negro ──
        Button(
            onClick = {
                localError = ""
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
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color.Gray
            )
        ) {
            if (state is AuthState.Loading)
                CircularProgressIndicator(
                    Modifier.size(20.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            else
                Text("Crear Cuenta", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))

        // ── Ya tienes cuenta en negro ──
        TextButton(onClick = onGoLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = Color.Black)
        }
    }
}