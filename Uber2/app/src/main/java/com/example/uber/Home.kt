package com.example.uber

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle

@Composable
fun Home () {
    backgroundHome()
}
@Composable
fun backgroundHome() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp) // Margen para que no pegue a los bordes
    ) {
        // 1. Margen superior para la barra de estado
        Spacer(modifier = Modifier.height(48.dp))
        // 2. Foto de cuenta (la mandamos al final/derecha)
        AccountImage(
            modifier = Modifier.align(Alignment.End),
            onClick = { /* Acción */ }
        )
        // 3. Espacio dinámico para bajar el cuadro verde
        // En lugar de padding(top=335.dp), esto es más limpio:
        Spacer(modifier = Modifier.height(3.dp))
        // 4. El Cuadro Verde (Ahora sí obedece el tamaño que le des aquí)
        GreenImage(
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // Centra horizontalmente
                .width(375.dp)  // Ancho deseado
                .height(135.dp), // Alto deseado
            onClick = { /* Acción */ }
        )
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
            style = androidx.compose.ui.text.TextStyle(
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
        // Botón corregido (sin el offset negativo que lo sacaba del cuadro)
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