package com.example.uber.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleRideModal(
    onDismiss: () -> Unit,
    onConfirm: (date: String, time: String) -> Unit
) {
    var dayExpanded  by remember { mutableStateOf(false) }
    var timeExpanded by remember { mutableStateOf(false) }
    // ── Estado del selector de día ──
    val today = remember { Calendar.getInstance() }
    val days = remember {
        (0..6).map { offset ->
            Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, offset) }
        }
    }
    val dayLabels = remember {
        val sdf = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
        days.map { sdf.format(it.time) }
    }
    var selectedDayIndex by remember { mutableStateOf(0) }

    // ── Estado del selector de hora ──
    val hours = remember { (0..23).flatMap { h -> listOf(0, 30).map { m -> h to m } } }
    val timeLabels = remember {
        hours.map { (h, m) ->
            val amPm = if (h < 12) "AM" else "PM"
            val hour12 = when { h == 0 -> 12; h > 12 -> h - 12; else -> h }
            "%d:%02d %s".format(hour12, m, amPm)
        }
    }
    val nowIndex = remember {
        val h = today.get(Calendar.HOUR_OF_DAY)
        val m = today.get(Calendar.MINUTE)
        hours.indexOfFirst { (hh, mm) -> hh > h || (hh == h && mm >= m) }.coerceAtLeast(0)
    }
    var selectedTimeIndex by remember { mutableStateOf(nowIndex) }

    // ── Scroll states ──
    val dayScrollState    = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val timeScrollState   = rememberLazyListState(initialFirstVisibleItemIndex = nowIndex)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .width(375.dp)
                .wrapContentHeight()
                .background(Color.White)
                .align(Alignment.CenterHorizontally)
        ) {

            // ── Título ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Schedule a Ride",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                    ),
                    modifier = Modifier
                        .width(176.dp)
                        .height(29.dp),
                    textAlign = TextAlign.Center
                )
            }

            // ── Separador full width ──
            Divider(color = Color(0xFFC5C5C5), thickness = 0.7.dp)

            // ── Selector de Día ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (dayExpanded) 150.dp else 72.dp), // ← crece al expandir
                contentAlignment = Alignment.Center
            ) {
                if (!dayExpanded) {
                    // ── Vista colapsada: solo muestra el valor ──
                    Text(
                        text = dayLabels[selectedDayIndex],
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(350),
                            color = Color(0xFF000000),
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { dayExpanded = true } // ← abre el scroll
                    )
                } else {
                    // ── Vista expandida: scroll ──
                    LazyColumn(
                        state = dayScrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(dayLabels) { index, label ->
                            Text(
                                text = label,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = if (index == selectedDayIndex) FontWeight(700) else FontWeight(400),
                                    color = if (index == selectedDayIndex) Color(0xFF000000) else Color(0xFF9E9E9E),
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedDayIndex = index
                                        dayExpanded = false // ← cierra al seleccionar
                                    }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // ── Separador centrado 325dp ──
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Divider(
                    color = Color(0xFFC5C5C5),
                    thickness = 0.7.dp,
                    modifier = Modifier.width(325.dp)
                )
            }

            // ── Selector de Hora ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (timeExpanded) 150.dp else 72.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!timeExpanded) {
                    Text(
                        text = timeLabels[selectedTimeIndex],
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(350),
                            color = Color(0xFF000000),
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { timeExpanded = true }
                    )
                } else {
                    LazyColumn(
                        state = timeScrollState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(timeLabels) { index, label ->
                            Text(
                                text = label,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = if (index == selectedTimeIndex) FontWeight(700) else FontWeight(400),
                                    color = if (index == selectedTimeIndex) Color(0xFF000000) else Color(0xFF9E9E9E),
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedTimeIndex = index
                                        timeExpanded = false
                                    }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // ── Separador full width ──
            Divider(color = Color(0xFFC5C5C5), thickness = 0.7.dp)

            // ── Botón Set pickup time ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onConfirm(dayLabels[selectedDayIndex], timeLabels[selectedTimeIndex])
                        onDismiss()
                    },
                    modifier = Modifier
                        .width(343.dp)
                        .height(49.dp),
                    shape = RoundedCornerShape(2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Set pickup time",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }
            }
        }
    }
}

