package com.example.ubercloneapp.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
// ↑ AsyncImage = composable de Coil que carga imágenes desde URL.
// Maneja descarga, caché en memoria y disco, y placeholder.

import com.example.ubercloneapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileVm: ProfileViewModel) {

    // ── Launcher para elegir imagen de la galería ──
    // ActivityResultContracts.GetContent() abre el selector de archivos.
    // "image/*" = solo imágenes (jpg, png, webp...).
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // ↑ Callback: cuando el usuario elige una imagen,
        // recibimos su Uri (dirección local del archivo).
        uri?.let { profileVm.uploadPhoto(it) }
        // ↑ Si no es null (el usuario no canceló), subir a Storage.
    }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

        // ── Foto de perfil circular ──
        if (profileVm.photoUrl != null) {
            AsyncImage(
                model = profileVm.photoUrl,
                // ↑ URL de Firebase Storage. Coil la descarga y cachea.
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(120.dp).clip(CircleShape),
                // ↑ clip(CircleShape) recorta la imagen en círculo.
                contentScale = ContentScale.Crop
                // ↑ Crop = escala la imagen para llenar el espacio,
                // recortando lo que sobre. Perfecto para avatares.
            )
        } else {
            // Placeholder cuando no hay foto
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text("👤",
                    modifier = Modifier.wrapContentSize(),
                    style = MaterialTheme.typography.headlineLarge)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón para cambiar foto
        OutlinedButton(
            onClick = { galleryLauncher.launch("image/*") },
            // ↑ Abre el selector de imágenes del sistema.
            enabled = !profileVm.isUploading
        ) {
            if (profileVm.isUploading)
                CircularProgressIndicator(Modifier.size(16.dp))
            else
                Text("📷 Cambiar foto")
        }

        Spacer(Modifier.height(32.dp))

        // ── Datos del usuario ──
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(24.dp)) {
                Text("Email", style = MaterialTheme.typography.labelMedium)
                Text(profileVm.email,
                    style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text("Total de viajes",
                    style = MaterialTheme.typography.labelMedium)
                Text("${profileVm.totalRides} viajes",
                    style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}