package com.example.uber


import android.app.Activity
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uber.navigation.AppNavigation
import com.example.uber.navigation.Routes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 1. Definir el NavController
            val navController = rememberNavController()

            // 2. Definir los ViewModels
            // La función viewModel() se encarga de crearlos o recuperarlos si ya existen
            val authVm: AuthViewModel = viewModel()

            // 3. Pasarlos al AppNavigation
            AppNavigation(
                navController = navController,
                authVm = authVm,
            )
        }
    }
}
@Composable
fun pantalla_inicial(onNavigateToLogin: () -> Unit){
    Screen()
    ImageUber()
    ImageCar()
    TextSafety()
    OnBoardBttn(onClick=onNavigateToLogin)
}
@Composable
fun PantallaLogin(authVm: AuthViewModel, navController: NavController) {
    val context = LocalContext.current as Activity

    // Escuchar cuando el código SMS es enviado para cambiar de pantalla
    LaunchedEffect(authVm.authState) {
        if (authVm.authState is AuthState.CodeSent) {
            navController.navigate(Routes.OTP)
        }
    }

    // Usamos Box para superponer los elementos sobre el fondo blanco
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // 1. Título
        Textmobile()

        // 2. Bloque de Teléfono (Posicionado exactamente)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 93.dp, start = 35.dp, end = 35.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardNumber(authVm.countryCode) { authVm.countryCode = it }
            CardNumber2(authVm.phoneNumber) { authVm.phoneNumber = it }
        }

        // 3. Textos de advertencia y botones sociales (tus funciones originales)
        Textmobile2()
        Textmobile3()

        // 4. Botones con lógica
        LoginBttn(onClick = { authVm.sendVerificationCode(context) })
        LoginBttnFb(onClick = { /* Lógica FB */ })
        LoginBttnG(onClick = { /* Lógica Google */ })

        // Indicador de carga
        if (authVm.authState is AuthState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        }
    }
}

@Composable
fun PantallaOTP(
    authVm: AuthViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val state = viewModel.authState
    var currentCode by remember { mutableStateOf("") }

    // el estado  cambia para al Home
    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            onSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextOTP(viewModel.phoneNumber)

        Squares_OTP()

        Putbttns(onClick = { }) // reenviar
        Putbttns2(onClick = { }) // login con contraseña

        IconPbttn(onClick = onBack)

        IconPbttn2(onClick = {
            if (currentCode.length == 6) {
                viewModel.verifyOtp(currentCode)
            }
        })

        // Indicador de carga si Firebase está validando
        if (state is AuthState.Loading) {
            androidx.compose.material3.LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                color = Color.Black
            )
        }

        if (state is AuthState.Error) {
            androidx.compose.material3.Text(
                text = "Invalid code, try again",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center).padding(top = 180.dp)
            )
        }
    }
}


@Composable
fun ScreenOTP(){
    Squares_OTP()
    Putbttns(onClick = {})
    Putbttns2(onClick = {})
    IconPbttn(onClick = {})
    IconPbttn2(onClick = {})
    }

@Composable
fun SafetyAlert () {
    bckgrndI (onClick = {})
    Texts()
    SABttn1(onClick = {})
}

