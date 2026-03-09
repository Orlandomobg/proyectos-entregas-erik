package com.example.uber


import android.app.Activity
import android.os.Bundle


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uber.navigation.AppNavigation
import com.example.uber.navigation.Routes
import com.example.uber.ui.components.CountrySelectorScreen
import com.example.uber.ui.screen.IconPbttn
import com.example.uber.ui.screen.IconPbttn2
import com.example.uber.ui.screen.ImageCar
import com.example.uber.ui.screen.ImageUber
import com.example.uber.ui.screen.LoginBttn
import com.example.uber.ui.screen.LoginBttnEmail
import com.example.uber.ui.screen.LoginBttnG
import com.example.uber.ui.screen.OnBoardBttn
import com.example.uber.ui.screen.Putbttns
import com.example.uber.ui.screen.Putbttns2
import com.example.uber.ui.screen.SABttn1
import com.example.uber.ui.screen.Screen
import com.example.uber.ui.screen.Squares_OTP
import com.example.uber.ui.screen.TextOTP
import com.example.uber.ui.screen.TextSafety
import com.example.uber.ui.screen.Textmobile
import com.example.uber.ui.screen.Textmobile2
import com.example.uber.ui.screen.Textmobile3
import com.example.uber.ui.screen.Texts
import com.example.uber.ui.screen.bckgrndI
import com.example.uber.viewmodel.AuthState
import com.example.uber.viewmodel.AuthViewModel
import com.example.uber.viewmodel.RideViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Uber)
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
                authVm = authVm
            )
        }
    }
}


@Composable
fun pantalla_inicial(onNavigateToLogin: () -> Unit){
    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
    }
    Screen()
    ImageUber()
    ImageCar()
    TextSafety()
    OnBoardBttn(onClick = onNavigateToLogin)
}
@Composable
fun PantallaLogin(authVm: AuthViewModel, navController: NavController, onLoginOk: () -> Unit,onLoginOk1: () -> Unit) {
    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true // ← iconos blancos
        )
    }
    val context = LocalContext.current
    val activity = context as? Activity ?: return

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
            CountrySelectorScreen(authVm = authVm)
        }

        // 3. Textos de advertencia y botones sociales (tus funciones originales)
        Textmobile2()
        Textmobile3()

        // 4. Botones con lógica
        LoginBttn(onNavigateToCode = {
            authVm.sendVerificationCode(activity)
        })
        LoginBttnEmail(onClick = { onLoginOk1()})
        LoginBttnG(authVm = authVm, onLoginOk = onLoginOk)

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
    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
    }
    val state = authVm.authState
    var currentCode by remember { mutableStateOf("") } // ✅ Estado elevado

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onSuccess()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextOTP(authVm.phoneNumber)
        Squares_OTP(
            code = currentCode,
            onCodeChange = { currentCode = it }
        )

        Putbttns(onClick = { })
        Putbttns2(onClick = { })

        IconPbttn(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 15.dp, bottom = 32.dp),
            onClick = {
                authVm.resetState()
                onBack()
            }
        )

        IconPbttn2(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 15.dp, bottom = 32.dp),
            onClick = {
                if (currentCode.length == 6) authVm.verifyOtp(currentCode)
            }
        )

        if (state is AuthState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                color = Color.Black
            )
        }
        if (state is AuthState.Error) {
            Text(
                text = "Código inválido, inténtalo de nuevo",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center).padding(top = 180.dp)
            )
        }
    }
}


@Composable
fun ScreenOTP(){
    Putbttns(onClick = {})
    Putbttns2(onClick = {})
    IconPbttn(onClick = {})
    IconPbttn2(onClick = {})
    }

@Composable
fun SafetyAlert (onNavigateToHome: () -> Unit) {
    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true // ← iconos blancos
        )
    }
    bckgrndI(onClick = {})
    Texts()
    SABttn1(onClick = { onNavigateToHome() })
}

