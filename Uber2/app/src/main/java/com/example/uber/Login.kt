package com.example.uber


import android.os.Bundle
import android.text.Layout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uber.ui.theme.UberTheme

@Composable
fun ScreenLogin () {
    Scaffold( containerColor = Color(0xFFFFFFFF)) { }
}

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
    val flagRes: Int // ID de la imagen de la bandera
)

/* NO SE USA PORQUE TINES UNA MEJOR VERSION EN INPUTLOGIN
@Composable
fun CardNumber(selectedCode: String, onCodeChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    // Lo mantenemos como Box con clickable para que se vea EXACTO a tu diseño original
    Box(modifier = Modifier
        .width(104.dp)
        .height(49.dp)
        .background(color = Color(0xFFEEEEEE))
        .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Aquí puedes poner la imagen de la bandera si la tienes
            Text(
                text = selectedCode,
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("España (+34)") },
                onClick = { onCodeChange("+34"); expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Algeria (+213)") },
                onClick = { onCodeChange("+213"); expanded = false }
            )
        }
    }
}
*/

/* NO SE USA YA QUE TIENES LA VER DE LISTA DE PAISES
@Composable
fun CardNumber2(phoneNumber: String, onNumberChange: (String) -> Unit) {
    Box(modifier = Modifier
        .width(232.dp)
        .height(49.dp)
        .background(color = Color(0xFFEEEEEE))
    ) {
        BasicTextField(
            value = phoneNumber,
            onValueChange = onNumberChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 15.dp),
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                if (phoneNumber.isEmpty()) {
                    Text(
                        text = "Mobile number",
                        color = Color(0xFF747474),
                        fontSize = 14.sp,
                        modifier = Modifier.offset(x = 38.dp) // Alineado a tu diseño
                    )
                }
                innerTextField()
            }
        )
    }
}

 */
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
fun LoginBttnFb (onClick: () -> Unit) {
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
fun LoginBttnG (onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 422.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {Button(onClick = { onClick() },shape = RectangleShape, modifier = Modifier
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
