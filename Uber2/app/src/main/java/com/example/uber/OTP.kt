package com.example.uber

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.contentColorFor
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextOTP(phoneNumber: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 58.dp, start = 16.dp),
    ) {
        Text(
            text = "Enter the 6-digit code sent you at $phoneNumber",
            style = TextStyle(
                fontSize =20.sp,
                lineHeight = 23.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000),
            ),
            modifier = Modifier
                .width(274.dp)
                .height(46.dp)
        )
    }
}

@Composable
fun Squares_OTP(){
    var d1 by remember { mutableStateOf("") }
    var d2 by remember { mutableStateOf("") }
    var d3 by remember { mutableStateOf("") }
    var d4 by remember { mutableStateOf("") }
    var d5 by remember { mutableStateOf("") }
    var d6 by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier.padding(top = 120.dp, start = 16.dp)
    ){
        Codebox(d1){ if (it.length <= 1) d1 = it }
        Codebox(d2){ if (it.length <= 1) d2 = it }
        Codebox(d3){ if (it.length <= 1) d3 = it }
        Codebox(d4){ if (it.length <= 1) d4 = it }
        Codebox(d5){ if (it.length <= 1) d5 = it }
        Codebox(d6){ if (it.length <= 1) d6 = it }
    }
}

@Composable
fun Codebox(
    value:String,
    onValueChange: (String) -> Unit){

    TextField(
        value = value,
        onValueChange = onValueChange,
        Modifier
            .width(54.dp)
            .height(49.dp)
            .background(color = Color(0xFFEEEEEE)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

    )
}

@Composable
fun Putbttns(onClick: () -> Unit){
    Column(
        modifier = Modifier.padding(top =203.dp, start = 16.dp)
    ) {
       Button(
           onClick = { onClick() },
           modifier= Modifier
               .width(185.dp)
               .height(50.dp)
               .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(size = 25.dp)),
           colors = ButtonDefaults.buttonColors(
               containerColor = Color(0xFFEEEEEE),
               contentColor = Color.Black
           )
       ){
           Text(
               text = "I didn’t recive a code",
               maxLines = 1,
               textAlign = TextAlign.Center,
               modifier = Modifier.fillMaxWidth(),
               style = TextStyle(
                   fontSize = 15.sp,
                   fontWeight = FontWeight(500),
                   color = Color(0xFF000000),
                   )
           )
       }
    }
}
@Composable
fun Putbttns2(onClick: () -> Unit){
    Column(
        modifier = Modifier.padding(top =263.dp, start = 16.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = { onClick() },
            modifier= Modifier
                .width(195.dp)
                .height(50.dp)
                .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(size = 25.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEEEEE),
                contentColor = Color.Black
            )
        ){
            Text(
                text = "Login with password",
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF000000),
                )
            )
        }
    }
}

@Composable
fun IconPbttn(onClick: () -> Unit) {
    Column(modifier = Modifier.padding(top = 770.dp, start = 15.dp)
        .fillMaxWidth()) {
        LargeFloatingActionButton(
            onClick = { onClick() },
            shape = CircleShape,
            modifier = Modifier
                .padding(1.dp)
                .width(60.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            containerColor = Color(0xFFEEEEEE),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.vector__2_),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)

            )
        }
    }
}

@Composable
fun IconPbttn2(onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(top = 770.dp, end = 15.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.End
        ) {

        LargeFloatingActionButton(
            onClick = { onClick() },
            shape = CircleShape,
            modifier = Modifier
                .padding(1.dp)
                .width(96.dp)
                .height(60.dp)
                .background(color = Color.Transparent),
            containerColor = Color(0xFFEEEEEE),
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()){

            Text(
                text = "Next",
                modifier = Modifier.align(Alignment.Center).offset(x = (-10).dp),
                style = TextStyle(fontSize = 17.sp)
            )

            Image(
                painter = painterResource(id = R.drawable.vector__3_),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterEnd).offset(x = (-20).dp),


            )

        }
        }
    }
}
/*@Composable
fun Dynbttn(
    text: String, onClick: () -> Unit){

    Button(
        onClick = { onClick() },
        contentPadding = PaddingValues( vertical = 0.dp),
        modifier =Modifier
            .width(145.dp)
            .height(34.dp)
            .background(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(size = 1000.dp)),
        shape = RoundedCornerShape(size = 1000.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEEEEEE),
            contentColor = Color.Black
        )
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFF000000),
                ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}*/
