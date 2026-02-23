package com.example.uber

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Screen () {
    Scaffold( containerColor = Color(0xFF276EF1)) { }
}

@Composable
fun ImageUber () {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 97.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.uber_logo),
            contentDescription = null,
            modifier = Modifier
                .width(79.dp)
                .height(26.dp)
        )
    }
}

@Composable
fun ImageCar () {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 207.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ubercar),
            contentDescription = null,
            modifier = Modifier
                .width(174.dp)
                .height(199.dp)
        )
    }
}

@Composable
fun TextSafety () {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 446.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Move with Safety",
            style = TextStyle(fontSize = 32.sp, color = Color.White, fontWeight = FontWeight(500)))
    }
}

@Composable
fun OnBoardBttn (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 825.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() },shape = RectangleShape, modifier = Modifier
        .width(343.dp)
        .height(49.dp)
        .background( color = Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Get Started",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp)
            )

            Image(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterEnd)
            )

        }
    }}

}
