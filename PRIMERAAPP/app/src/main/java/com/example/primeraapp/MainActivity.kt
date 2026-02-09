package com.example.primeraapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.primeraapp.ui.theme.PRIMERAAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PRIMERAAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    calculadora(4.0,4.0,"sumar")
                    Hello()
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PRIMERAAPPTheme {
        Greeting("Android")
    }
}

@Composable
fun Clasificator (Edad: Int) {
    if (Edad < 18) {
        Text("eres menor")
    } else {
        Text("eres mayor")
    }
}

@Composable
fun calculadora (a: Double, b: Double, accion: String){
    if (accion == "sumar") {
        val resultado: Double  = a + b
        Text("$resultado")
    } else if (accion == "restar") {
        val resultado: Double  = a - b
        Text("$resultado")
    } else if (accion == "multiplicar") {
        val resultado: Double  = a * b
        Text("$resultado")
    }
}

data class Persona(
    val nombre: String,
    val telefono: String
) {
    fun llamar() {
        println("Llamando a $nombre con número $telefono")
    }
}

@Composable
fun PersonaView(persona: Persona) {
    Text(text = "Nombre: ${persona.nombre}")
}
@Composable
fun Hello () {
    println("Hello world")
}