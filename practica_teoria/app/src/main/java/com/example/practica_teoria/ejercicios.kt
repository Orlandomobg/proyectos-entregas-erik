package com.example.practica_teoria


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.test.espresso.base.Default
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete


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

data class Tarea(
    val id: Int,
    val texto: String,
    val completada: Boolean = false
)

@Composable
fun ListaDeTareas () {
    //Estado: lista de tareas
    var tareas by remember { mutableStateOf(
        listOf(
            Tarea(1,"comprar pan"),
            Tarea(2,"llamar al medico"),
            Tarea(3,"Estudiar compose")
                    )
        )
    }

    //Estado de la nueva tarea
    var nuevaTarea by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)){
        Text("Mis tareas",style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        //Seccion para agregar nueva tarea

        Row{
            TextField(
                value = nuevaTarea,
                onValueChange = {nuevaTarea = it },
                label = {Text("Nueva tarea.")},
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (nuevaTarea.isNotBlank()) {
                        //IMPORTANTE: creamos una nueva lista
                        tareas = tareas + Tarea(
                            id = tareas.size + 1,
                            texto = nuevaTarea
                        )
                        nuevaTarea = "" // limpiamos el campo
                    }
                }
            ){
                Text("Agregar")
            }
        }

    Spacer(modifier = Modifier.height(16.dp))

    // Mostramos cada tarea
    tareas.forEach{ tarea ->
        Row(modifier = Modifier.fillMaxWidth()
            .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
            ){
            //checkbox para marcar como completada
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { estaCompletada ->
                    //actualizamos solo esta tarea
                    tareas = tareas.map { t ->
                        if (t.id == tarea.id) {
                            t.copy (completada = estaCompletada)
                        } else { t }
                    }
                }
            )

            // texto de la tarea
            Text(text = tarea.texto,
                modifier = Modifier.weight(1f),
                textDecoration = if (tarea.completada) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                })

            // boton para eliminar
            IconButton(
                onClick = {
                    //filtramos para quitar esta tarea
                    tareas = tareas.filter{ it.id != tarea.id}
                }
            ) {
                Icon(Icons.Default.Delete,contentDescription =  "Eliminar")
            }

        }

    }

    //Contador
    val completadas = tareas.count() {it.completada}
        Text("Completadas:$completadas de ${tareas.size}")
    }
}










