package com.tteoli.core


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val fusedClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Location? =
        suspendCancellableCoroutine { cont ->
            // 1차: lastLocation 시도
            fusedClient.lastLocation
                .addOnSuccessListener { last ->
                    if (last != null) {
                        cont.resume(last)
                    } else {
                        // 2차: 실제 현재 위치 요청
                        val cts = CancellationTokenSource()
                        fusedClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            cts.token
                        ).addOnSuccessListener { current ->
                            cont.resume(current)
                        }.addOnFailureListener {
                            cont.resume(null)
                        }
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
        }
}
