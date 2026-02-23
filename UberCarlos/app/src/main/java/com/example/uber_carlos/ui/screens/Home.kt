package com.example.uber_carlos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uber_carlos.R
import com.example.uber_carlos.viewmodel.AuthViewModel

@Composable
fun Home(viewModel: AuthViewModel, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Logout",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                ),
                modifier = Modifier.clickable {
                    viewModel.logout()
                    onLogout()
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        RectangleHome()
    }
}

@Composable
fun RectangleHome() {
    Box(
        modifier = Modifier
            .height(135.dp)
            .width(343.dp)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
            .background(color = Color(0xFF10462E), shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)) // para que la imagen no se salga de las esquinas
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Satisfy any carving",
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontFamily = FontFamily(Font(R.font.uber_move_text)),
                        fontWeight = FontWeight(500),
                        color = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Order on Eats",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.uber_move_text)),
                            fontWeight = FontWeight(500),
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.fondo_home),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(140.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
