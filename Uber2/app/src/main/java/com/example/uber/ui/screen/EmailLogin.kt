package com.example.uber.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.viewmodel.AuthState
import com.example.uber.viewmodel.AuthViewModel


@Composable
fun EmailScreen(authVm: AuthViewModel,
                onLoginOk:   () -> Unit,
                onGoRegister: () -> Unit,
                onBack: () -> Unit){

    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = authVm.authState

    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onLoginOk()
    }
    Column(
    modifier = Modifier
    .fillMaxSize()
    .background(color = Color.White)
    .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(200.dp))

        Text("Iniciar Sesión",style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight(500),
            color = Color.Black,
        ) )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.LightGray) },
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

        if (state is AuthState.Error) {
            Text(state.msg, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = { authVm.login(email.trim(), password) },
            enabled = state !is AuthState.Loading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,  // ← fondo gris
                contentColor = Color.Black,         // ← letras negras
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
                Text("Iniciar Sesión", color = Color.Black)
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = onGoRegister) {
            Text("¿No tienes cuenta? Regístrate",color = Color.Black)
        }

        Spacer(Modifier.height(250.dp))

        IconPbttn(modifier = Modifier
            .padding(start = 15.dp, bottom = 32.dp),
            onClick = {
                authVm.resetState()
                onBack()
            })

    }
}

