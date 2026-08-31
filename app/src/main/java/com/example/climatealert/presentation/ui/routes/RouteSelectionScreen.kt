package com.example.climatealert.presentation.ui.routes

import android.content.Context
import android.graphics.Color as GraphicsColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import com.example.climatealert.domain.model.RouteOption
import com.example.climatealert.presentation.ui.routes.RouteSelectionUiState

// Initialize osmdroid configuration (only once)
@Composable
fun rememberOsmdroidContext() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        Configuration.getInstance().userAgentValue = "ClimateAlert/1.0"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionScreen(
    navController: NavController,
    viewModel: RouteSelectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Initialize osmdroid once
    rememberOsmdroidContext()

    // Fetch routes on enter (replace with actual GPS + destination)
    LaunchedEffect(Unit) {
        viewModel.fetchRoutes(
            startLat = 28.6139,
            startLon = 77.2090,
            endLat = 28.7041,
            endLon = 77.1025
        )
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { navController.popBackStack() },
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            // ---- Map Section with osmdroid ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
            ) {
                // Create the osmdroid MapView
                val mapView = rememberMapView()
                AndroidView(
                    factory = { mapView },
                    modifier = Modifier.fillMaxSize()
                ) { view ->
                    // This block runs when the view is ready
                    drawRoutesOnMap(view, uiState)
                }

                // Loading overlay
                if (uiState is RouteSelectionUiState.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // ---- Bottom Sheet Content (unchanged) ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Route options",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (val state = uiState) {
                    is RouteSelectionUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is RouteSelectionUiState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("❌ ${state.message}", color = MaterialTheme.colorScheme.onErrorContainer)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = {
                                    viewModel.fetchRoutes(28.6139, 77.2090, 28.7041, 77.1025)
                                }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }

                    is RouteSelectionUiState.Success -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 350.dp)
                        ) {
                            items(state.routes) { route ->
                                RouteCard(
                                    route = route,
                                    isSelected = route.id == state.selectedRouteId,
                                    onClick = { viewModel.selectRoute(route.id) }
                                )
                            }

                            item {
                                Button(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ComposeColor(GraphicsColor.parseColor("#34C759"))),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "Start coolest route",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ComposeColor.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---- Helper: Create and configure osmdroid MapView ----
@Composable
fun rememberMapView(): MapView {
    val context = LocalContext.current
    return remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            // Optional extras
            val rotationGestureOverlay = RotationGestureOverlay(context, this)
            rotationGestureOverlay.isEnabled = true
            this.overlays.add(rotationGestureOverlay)

            val compassOverlay = CompassOverlay(context, this)
            compassOverlay.enableCompass()
            this.overlays.add(compassOverlay)
        }
    }
}

// ---- Helper: Draw routes on the osmdroid map ----
private fun drawRoutesOnMap(mapView: MapView, uiState: RouteSelectionUiState) {
    if (uiState !is RouteSelectionUiState.Success) return

    // Clear previous overlays (keep compass/rotation if needed)
    mapView.overlays.clear()
    // Re-add any persistent overlays (compass, rotation) if you added them in rememberMapView
    // For simplicity, we'll just add polylines and let the user use pinch-to-zoom.

    val selectedId = uiState.selectedRouteId
    val allPoints = mutableListOf<GeoPoint>()

    uiState.routes.forEach { route ->
        val routeColor = if (route.id == selectedId) {
            GraphicsColor.parseColor("#007AFF")
        } else {
            GraphicsColor.parseColor("#8E8E93")
        }
        val routeWidth = if (route.id == selectedId) 12f else 6f

        val polyline = Polyline().apply {
            setPoints(route.polylinePoints)
            color = routeColor
            outlinePaint.strokeWidth = routeWidth
            isGeodesic = true // approximate great-circle lines
        }
        mapView.overlays.add(polyline)
        allPoints.addAll(route.polylinePoints)
    }

    // Auto-zoom to fit all routes
    if (allPoints.isNotEmpty()) {
        val boundingBox = BoundingBox.fromGeoPoints(allPoints)
        mapView.zoomToBoundingBox(boundingBox, true)
    }

    mapView.invalidate()
}

// ---- RouteCard composable (unchanged, but uses the new RouteOption) ----
@Composable
fun RouteCard(route: RouteOption, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ComposeColor(GraphicsColor.parseColor("#E3F2FD")) else ComposeColor(GraphicsColor.parseColor("#F8F9FA"))
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = ComposeColor(GraphicsColor.parseColor("#007AFF"))
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = route.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${route.shadePercent}% shaded", color = ComposeColor(GraphicsColor.parseColor("#007AFF")), fontSize = 14.sp)
                    if (route.pointsEarned != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+${route.pointsEarned} pts",
                            color = ComposeColor(GraphicsColor.parseColor("#FF9500")),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                route.exposureWarning?.let {
                    Text(text = it, color = ComposeColor.Red, fontSize = 14.sp)
                }
            }
            Text(
                text = "${route.timeMinutes} min",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


