package com.example.climatealert.domain.model

import org.osmdroid.util.GeoPoint

data class RouteOption(
    val id: String,
    val name: String,
    val shadePercent: Int,
    val timeMinutes: Int,
    val pointsEarned: Int? = null,
    val exposureWarning: String? = null,
    val polylinePoints: List<GeoPoint>
)