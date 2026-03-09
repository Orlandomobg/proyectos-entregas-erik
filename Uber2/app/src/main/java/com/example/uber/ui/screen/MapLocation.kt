package com.example.uber.ui.screen


import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
// ↑ Contiene las constantes de permisos de Android.
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
// ↑ Nos da el Context de Android dentro de un composable.
// Necesario para crear el FusedLocationProviderClient.
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
// ↑ Librería Accompanist: pedir permisos de forma declarativa en Compose.

import com.google.android.gms.location.LocationServices
// ↑ Punto de entrada a los servicios de ubicación de Google.
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.uber.R
import com.example.uber.viewmodel.RideViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    rideVm: RideViewModel,
    onRequestRide: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {

    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = true
        )
    }

    val context = LocalContext.current
    var destination by remember { mutableStateOf("") }
    var showExtraStop by remember { mutableStateOf(false) }

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let { rideVm.updateUserLocation(LatLng(it.latitude, it.longitude)) }
                }
            } catch (e: SecurityException) {}
        }
    }

    val userLoc = rideVm.userLocation
    val defaultLocation = LatLng(40.4168, -3.7038)
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLoc ?: defaultLocation, 15f)
    }
    LaunchedEffect(userLoc) {
        userLoc?.let {
            cameraPosition.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ── Botón atrás ──
        Spacer(modifier = Modifier.height(55.dp))

        Box(
            modifier = Modifier
                .offset(x=20.dp)
                .size(30.dp)
                .background(color = Color.White)
                .clickable { onBack() },
            contentAlignment = Alignment.Center,

        ) {
            Image(
                painterResource(id = R.drawable.vector__2_), "back", Modifier
                    .padding(1.dp)
                    .size(24.dp)
                    .background(color = Color.White)
                , contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(18.dp))


        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(280.dp)
                .height(34.dp)
                .offset(x = -13.dp)
                .background(color = Color(0xFFF9F9F9)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Unnamed Road",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF53585E),
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            BasicTextField(
                value = destination,
                onValueChange = { destination = it },
                modifier = Modifier
                    .width(300.dp)
                    .height(34.dp)
                    .background(color = Color(0xFFEDEDED)),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (destination.isEmpty()) {
                            Text(
                                text = "Go to pin",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(350),
                                    color = Color(0xFFA4A5AA),
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // ── Botón + ──
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color.White)
                    .offset(x=18.dp)
                    .clickable { showExtraStop = !showExtraStop },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vector__14_),
                    contentDescription = "add stop",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(1.dp)
                        .size(18.dp)
                )
            }
        }

        // ── Input extra (se despliega al pulsar +) ──
        if (showExtraStop) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp)
                    .height(34.dp)
                    .offset(x = -13.dp)
                    .background(color = Color(0xFFEDEDED)),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Add stop",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFA4A5AA),
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ── Mapa ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(695.dp)
        ) {
            if (locationPermission.status.isGranted) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPosition,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false, // ← quitamos controles default
                        myLocationButtonEnabled = true
                    )
                ) {
                    userLoc?.let {
                        Marker(
                            state = rememberMarkerState(position = it),
                            title = "Tú estás aquí"
                        )
                    }
                }
            } else {
                Column(
                    Modifier.fillMaxSize().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Necesitamos tu ubicación")
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { locationPermission.launchPermissionRequest() }) {
                        Text("Conceder permiso")
                    }
                }
            }

            // ── Botón Done dentro del mapa ──
            Button(
                onClick = onRequestRide,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .width(370.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp)
            ) {
                Text(
                    text = "Done",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
        }
    }
}