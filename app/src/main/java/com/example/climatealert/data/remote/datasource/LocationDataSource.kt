package com.example.climatealert.data.remote.datasource

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationDataSource @Inject constructor(private val context: Context) {
    private val client: FusedLocationProviderClient by lazy { 
        LocationServices.getFusedLocationProviderClient(context) 
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        return try {
            val location = client.getLastLocationSuspend()
            location?.let { it.latitude to it.longitude }
        } catch (e: Exception) {
            null
        }
    }
}

private suspend fun FusedLocationProviderClient.getLastLocationSuspend(): Location? =
    suspendCancellableCoroutine { cont ->
        lastLocation.addOnSuccessListener { location ->
            cont.resume(location)
        }.addOnFailureListener {
            cont.resume(null)
        }
    }