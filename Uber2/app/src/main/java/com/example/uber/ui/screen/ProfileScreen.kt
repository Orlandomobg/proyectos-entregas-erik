package com.example.uber.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.viewmodel.AuthViewModel


@Composable
fun ProfileScreen(onLogout: () -> Unit, onBack: () -> Unit
) {

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))

            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                Text("👤",
                    modifier = Modifier.wrapContentSize(),
                    style = MaterialTheme.typography.headlineLarge)
            }

        Spacer(Modifier.height(32.dp))

        Button( onClick = {onLogout()},
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = buttonColors(containerColor = Color.LightGray)

        ) {
            Text(text = "Logout",
                color = Color.Black,
                fontSize = 21.sp,
            )
        }

        Spacer(Modifier.height(490.dp))

        IconPbttn(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(start = 15.dp, bottom = 32.dp),
            onClick = {onBack()}
        )
    }
}