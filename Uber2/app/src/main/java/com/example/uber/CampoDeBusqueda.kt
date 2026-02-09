package com.example.uber

import android.R.attr.value
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CampoDeBusqueda () {
    // estado para guardar el texto
    var textoBusqueda by remember { mutableStateOf("") }

    Column(modifier  = Modifier.padding(16.dp)) {
        // campo donde el usuario escribe
        TextField(
            value = textoBusqueda,
            onValueChange = {textNuevo ->
            textoBusqueda = textNuevo
                            },
            label = {Text("Buscar productos")}
        )
        // mostramos lo que escribio
        if (textoBusqueda.isNotEmpty()) {
            Text("Buscando : $textoBusqueda")
        }
    }
}


@Composable
fun PantallaConfiguracion () {
    //Tres estados independientes
    var modoOscuro by remember { mutableStateOf(false)  }
    var notificaciones  by remember { mutableStateOf(true) }
    var sonido by remember { mutableStateOf(true) }

    Scaffold(containerColor = if(modoOscuro) Color.Black else Color.White,
        contentColor = if(modoOscuro)Color.White else Color.Black
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Configuracion", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(20.dp))

            //Switch para modo oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Modo Oscuro")
                Switch(
                    checked = modoOscuro,
                    onCheckedChange = { nuevoValor ->
                        modoOscuro = nuevoValor
                    })
            }

            // checkbox para notificaciones
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = notificaciones,
                    onCheckedChange = { notificaciones ->
                        true
                    }
                )
                Text("Recibir notificaciones")
            }

            // Checkbox para sonido (solo si las notificaciones esta activo)
            if (notificaciones) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = sonido,
                        onCheckedChange = { sonido -> true }
                    )
                    Text("Con sonido")
                }
            }
        }
    }
}































