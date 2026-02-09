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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.ui.theme.UberTheme

@Composable
fun ScreenLogin () {
    Scaffold( containerColor = Color(0xFFFFFFFF)) { }
}

@Composable
fun LoginBttn (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 175.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() }, modifier = Modifier
        .width(343.dp)
        .height(49.dp)
        .background( color = Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Next",
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

@Composable
fun LoginBttnFb (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 357.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() }, modifier = Modifier
        .width(343.dp)
        .border(width = 0.7.dp, color = Color(0xFF000000),shape = RoundedCornerShape(size = 2.dp))
        .height(49.dp)
        .background( color = Color.White)
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black,

        )
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Continue with Facebook",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp,
                    fontWeight = FontWeight(500))
            )

            Image(
                painter = painterResource(id = R.drawable.image_4),
                contentDescription = null,
                modifier = Modifier
                    .width(23.dp)
                    .height(22.dp)
                    .align(Alignment.CenterStart)
            )

        }
    }}
}

@Composable
fun LoginBttnG (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 422.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() }, modifier = Modifier
        .width(343.dp)
        .height(49.dp)
        .border(width = 0.7.dp, color = Color(0xFF000000), shape = RoundedCornerShape(size = 2.dp))
        .background( color = Color.White),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Continue with Google",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp,
                    fontWeight = FontWeight(500))
            )

            Image(
                painter = painterResource(id = R.drawable.image_5),
                modifier = Modifier
                    .width(23.dp)
                    .height(22.dp)
                    .align(Alignment.CenterStart),
                contentDescription = null
            )

        }
    }}
}
