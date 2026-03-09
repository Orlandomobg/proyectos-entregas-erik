package com.example.uber.ui.screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.R
import com.example.uber.viewmodel.AuthState
import com.example.uber.viewmodel.AuthViewModel


@Composable
fun Textmobile () {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 58.dp, start = 35.dp),
    ) {
        Text(
            text = "Enter your mobile number",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000),
                ),
            modifier = Modifier
                .width(193.dp)
                .height(19.dp)
        )
    }
}

// logica del numero del telefono

data class Country(
    val name: String,
    val code: String,
    val flagRes: Int
)

@Composable
fun LoginBttn (onNavigateToCode: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 158.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onNavigateToCode() },shape = RectangleShape, modifier = Modifier
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
fun LoginBttnEmail (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 357.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() }, shape = RectangleShape,modifier = Modifier
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
                text = "Continue with Email",
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 17.sp,
                    fontWeight = FontWeight(500))
            )

            Image(
                painter = painterResource(id = R.drawable.email_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.CenterStart)
            )

        }
    }}
}
@Composable
fun Textmobile2() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 245.dp, start = 35.dp),
    ) {
        Text(
            text = "By continuing you may recive an SMS fo rverification. Message and data rates may apply",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.46.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF757575),

                ),
            modifier = Modifier
                .width(388.dp)
                .height(42.dp)
        )
    }
}

@Composable
fun Textmobile3() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 317.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "or",
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 21.46.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
                ),
            modifier = Modifier
                .width(388.dp)
                .height(42.dp)

        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally

        ){

        Image(
            painter = painterResource(id = R.drawable.vector_1__1_),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Start)
                .offset(x=35.dp,y=-33.dp))

        Image(
            painter = painterResource(id = R.drawable.vector_1__1_),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.End)
                .offset(x=-35.dp,y=-33.dp))
        }
    }
}

@Composable
fun LoginBttnG ( authVm: AuthViewModel,
                 onLoginOk:   () -> Unit) {
    val state = authVm.authState
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) onLoginOk()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 422.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { authVm.signInWithGoogle(context) },
        enabled = state !is AuthState.Loading,shape = RectangleShape, modifier = Modifier
        .width(343.dp)
        .height(49.dp)
        .border(width = 0.7.dp, color = Color(0xFF000000))
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
                painter = painterResource(id = R.drawable.gmaillogo),
                modifier = Modifier
                    .size(19.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 8.dp),
                contentDescription = null
            )

        }
    }}
}
