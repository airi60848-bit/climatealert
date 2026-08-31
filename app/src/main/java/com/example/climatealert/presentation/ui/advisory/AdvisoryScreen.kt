package com.example.climatealert.presentation.ui.advisory



import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.climatealert.domain.model.HourlyForecast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisoryScreen(
    navController: NavController,
    viewModel: AdvisoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather Advisory") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is AdvisoryUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is AdvisoryUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("⚠️ ${state.message}", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.fetchAdvisoryData() }) { Text("Retry") }
                }
            }

            is AdvisoryUiState.Success -> {
                val data = state.data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ---- Current Temp & Warning Title ----
                    item {
                        Column {
                            Text(
                                text = "Extreme heat warning",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Active until 6:00 PM today",
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Current: ${data.currentTemp}°C",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // ---- What to do (static tips) ----
                    item {
                        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color(0xFFF8F9FA))) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("What to do", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                listOf(
                                    "Stay hydrated",
                                    "Avoid direct sun, 11 AM–4 PM",
                                    "Move indoors if you feel dizzy"
                                ).forEach { tip ->
                                    Text(text = "• $tip", modifier = Modifier.padding(vertical = 4.dp))
                                }
                            }
                        }
                    }

                    // ---- Dynamic Cooling Center ----
                    item {
                        data.nearestCenter?.let { center ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(Color(0xFFE8F5E9))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Nearest cooling center", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column {
                                            Text(text = center.name, fontSize = 16.sp)
                                            Text(text = "· ${center.walkMinutes} min walk", color = Color.Gray)
                                        }
                                        Button(
                                            onClick = {
                                                val uri = Uri.parse("google.navigation:q=${center.lat},${center.lon}")
                                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                                        ) {
                                            Text("Navigate")
                                        }
                                    }
                                }
                            }
                        } ?: run {
                            Text("No cooling centers found nearby. Stay hydrated!", color = Color.Gray)
                        }
                    }

                    // ---- Dynamic Forecast ----
                    item {
                        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color(0xFFF8F9FA))) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Today's forecast", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    data.forecast.forEach { hour ->
                                        ForecastItem(hour)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = data.safetyMessage,
                                    color = Color(0xFFE65100),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForecastItem(forecast: HourlyForecast) {
    val isHot = forecast.tempCelsius >= 35
    val isWarm = forecast.tempCelsius >= 28
    val color = when {
        isHot -> Color.Red
        isWarm -> Color.Yellow
        else -> Color.Green
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(color, shape = RoundedCornerShape(4.dp))
        )
        Text(text = forecast.time, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        Text(text = "${forecast.tempCelsius}°", fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}