package com.example.uber.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.OnPlacedModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import android.Manifest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
@Composable
fun Home (onProfileClick: () -> Unit,onMapClick: () -> Unit) {
    backgroundHome(onProfileClick = {onProfileClick()}, onMapClick = {onMapClick()})
}
@Composable
fun backgroundHome(onProfileClick: () -> Unit,
                   onMapClick: () -> Unit
                   ) {
    val systemUiController = rememberSystemUiController()

    // fondo negro → iconos blancos
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true // ← iconos blancos
        )
    }

    var direction by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        AccountImage(
            modifier = Modifier.align(Alignment.End),
            onClick = { onProfileClick()}
        )

        Spacer(modifier = Modifier.height(3.dp))

        GreenImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(135.dp),
            onClick = {  }
        )
        Spacer(modifier = Modifier.height(19.dp))

        TextDestination(modifier =  Modifier.align(Alignment.CenterHorizontally) ,
            direction = direction,
            onDirectionChange = { direction = it },
            onClick = {}
        )

        Spacer(modifier = Modifier.height(2.dp))

        ChooseSavedPlace(onClick = {})

        Spacer(modifier = Modifier.height(2.dp))

        Image(
            painter = painterResource(id = R.drawable.vector_1__1_),
            contentDescription = "image description",
            contentScale = ContentScale.None,
            modifier = Modifier
                .border(width = 0.7.dp, color = Color(0xFFE0E0E0))
                .padding(0.7.dp)
                .width(325.dp)
                .height(0.dp)
                .align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(2.dp))

        SetDestinationMap(onClick = {onMapClick()})

        Image(
            painter = painterResource(id = R.drawable.vector_1__1_),
            contentDescription = "image description",
            contentScale = ContentScale.None,
            modifier = Modifier
                .border(width = 0.7.dp, color = Color(0xFFE0E0E0))
                .padding(0.7.dp)
                .width(325.dp)
                .height(0.dp)
                .align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(2.dp))

        AroundYou()

        Map()
    }
}
@Composable
fun AccountImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .size(50.dp)
        .clip(CircleShape)
        .clickable(
            onClick = onClick,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Composable
fun GreenImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .border(width = 1.dp, color = Color(0xFFDEE4E2), shape = RoundedCornerShape(size = 8.dp))
        .background(color = Color(0xFF10462E), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Satisfy any carving",
            style = TextStyle(
                fontSize = 21.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
            ),
            modifier = Modifier
                .offset(y = (38).dp,x = (17).dp)
        )

        Image(
            painter = painterResource(id = R.drawable.food),
            contentDescription = "comida",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(111.dp)
                .height(124.dp)
                .align(Alignment.CenterEnd)
                .offset(y = 5.5.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 65.dp)
                .width(130.dp)
                .offset(x = 6.dp)
        ) {
            Text(
                text = "Order on Eats",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF)
                ),
                modifier = Modifier.offset(x = (-8).dp)
            )
            Image(
                painter = painterResource(id = R.drawable.vector__7_),
                contentDescription = "icono",
                modifier = Modifier
                    .width(9.dp)
                    .height(9.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-12).dp),

            )

        }
    }
}

@Composable
fun TextDestination (modifier: Modifier = Modifier,
                     direction: String,
                     onDirectionChange: (String) -> Unit,
                     onClick: () -> Unit) {

    var showScheduleModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color(0xFFEDEDED)),
    ) {
        BasicTextField(
            value = direction,
            onValueChange = onDirectionChange , singleLine = true,
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier.width(190.dp)
                .height(210.dp).offset(x = 15.dp,y = 19.dp),
            decorationBox = { innerTextField1 ->
                if (direction.isEmpty()) {
                    Text(text = "Where To?",
                        style = TextStyle(
                            fontSize = 20.sp),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000),
                        )
                }
                innerTextField1()
            }
        )

        Image(
            painter = painterResource(id = R.drawable.vector_3__1_),
            contentDescription = "image description",
            modifier = Modifier
                .width(1.dp)
                .height(40.dp)
                .align(alignment = Alignment.CenterStart)
                .offset(x = 207.5.dp)
        )

        Button(
            onClick = { showScheduleModal = true },
            modifier = Modifier
                .width(130.dp)
                .height(42.dp)
                .align(alignment = Alignment.CenterStart)
                .offset(x = 228.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White),
            contentPadding = PaddingValues(horizontal = 15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.svgexport_1_1),
                    contentDescription = "image description",
                    modifier = Modifier.size(28.dp)
                        .padding(1.dp)
                )
                Text(
                    text = if (selectedDate.isEmpty()) "Now" else "$selectedDate $selectedTime",
                    style = TextStyle(
                        fontSize = if (selectedDate.isEmpty()) 16.sp else 11.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Image(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = "image description",
                    modifier = Modifier.padding(1.dp)
                        .width(12.dp)
                        .height(8.dp)
                )
            }
        }
    }

    if (showScheduleModal) {
        ScheduleRideModal(
            onDismiss = { showScheduleModal = false },
            onConfirm = { date, time ->
                selectedDate = date
                selectedTime = time
                println("Viaje programado: $date a las $time")
            }
        )
    }
}

@Composable
fun ChooseSavedPlace (modifier: Modifier = Modifier,
                      onClick: () -> Unit){
    Box( modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(color = Color.White)){

        Button(onClick = { onClick() },modifier = Modifier
            .fillMaxWidth().height(80.dp),
            shape = RoundedCornerShape(size = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White)) {

            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .offset(x = -60.dp)
            ) {
                // imagen de fondo
                Image(
                    painter = painterResource(id = R.drawable.ellipse_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // imagen encima
                Image(
                    painter = painterResource(id = R.drawable.star_black_24dp_1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = "Choose a saved place",
                style = TextStyle(
                    fontSize = 18.sp),
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                modifier = Modifier
                    .width(197.dp)
                    .height(22.dp)
                    .offset(x= -48.dp),

            )
            Image(
                painter = painterResource(id = R.drawable.vector__8_),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(15.dp)
                    .offset(x = 68.dp))

        }

    }
}

@Composable
fun SetDestinationMap (modifier: Modifier = Modifier,
                      onClick: () -> Unit){
    Box( modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .background(color = Color.White)){

        Button(onClick = { onClick() },modifier = Modifier
            .fillMaxWidth().height(80.dp),
            shape = RoundedCornerShape(size = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White)) {

            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .offset(x = -60.dp)
            ) {
                // imagen de fondo
                Image(
                    painter = painterResource(id = R.drawable.ellipse_2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // imagen encima
                Image(
                    painter = painterResource(id = R.drawable.vector__9_),
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = "Set distination on map",
                style = TextStyle(
                    fontSize = 18.sp),
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                modifier = Modifier
                    .width(197.dp)
                    .height(22.dp)
                    .offset(x= -48.dp),

                )
            Image(
                painter = painterResource(id = R.drawable.vector__8_),
                contentDescription = "image description",
                contentScale = ContentScale.None,
                modifier = Modifier
                    .size(15.dp)
                    .offset(x = 68.dp))

        }

    }
}

@Composable
fun AroundYou(){
    Box( modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .background(color = Color.White)){

        Text(
            text = "Around you",
            style = TextStyle(
                fontSize =20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                ),
            modifier = Modifier
                .width(150.dp)
                .height(21.dp)
                .align(alignment = Alignment.CenterStart)
                .offset(x= 10.dp)
        )
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map() {
    val context = LocalContext.current

    val locationPermission = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val defaultLocation = LatLng(40.4168, -3.7038)
    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }

    // ── Obtener ubicación ──
    LaunchedEffect(locationPermission.status.isGranted) {
        if (locationPermission.status.isGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedClient.lastLocation.addOnSuccessListener { loc ->
                    loc?.let {
                        userLocation = LatLng(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {}
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    // ── Mover cámara cuando llega ubicación ──
    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPosition.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(it, 15f)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (locationPermission.status.isGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                properties = MapProperties(isMyLocationEnabled = true),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false,
                    scrollGesturesEnabled = false, // ← desactiva scroll para que no interfiera con el scroll de la pantalla
                    zoomGesturesEnabled = false
                )
            ) {
                userLocation?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Tú estás aquí"
                    )
                }
            }
        } else {
            // ── Sin permiso: fondo gris con mensaje ──
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEDEDED)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap to enable location",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF757575)
                    ),
                    modifier = Modifier.clickable {
                        locationPermission.launchPermissionRequest()
                    }
                )
            }
        }
    }
}