package com.example.uber


import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.ui.theme.UberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UberTheme {
            }
            pantalla_inicial()
            pantalla_login()
        }
    }
}
@Composable
fun pantalla_inicial(){
    Screen()
    ImageUber()
    ImageCar()
    TextSafety()
    OnBoardBttn(onClick={})
}
@Composable
fun pantalla_login(){
    ScreenLogin()
    LoginBttn (onClick={})
    LoginBttnFb(onClick={})
    LoginBttnG (onClick={})
    Textmobile()
    CardNumber()
    CardNumber2()
    Textmobile2()
    Textmobile3()
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
    UberTheme {
        Greeting("Android")
    }
}

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

