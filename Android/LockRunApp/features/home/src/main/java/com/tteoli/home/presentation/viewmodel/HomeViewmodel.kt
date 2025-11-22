package com.tteoli.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tteoli.core.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

private const val TAG = "HomeViewmodel"
@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _elapsedSec = MutableStateFlow(0L)
    val elapsedSec: StateFlow<Long> = _elapsedSec
    private var job: Job? = null

    private val _timer = MutableStateFlow("00:00:00")
    val timer: StateFlow<String> = _timer

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

    fun startTimer() {
        if (job != null) return // 이미 돌고 있으면 무시

        job = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                _elapsedSec.update { it + 1 }
                _timer.update { formatSecondsToHms(elapsedSec.value) }
            }
        }
    }

    fun formatSecondsToHms(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return "%02d:%02d:%02d".format(h, m, s)
    }

    fun pauseTimer() {
        job?.cancel()
        job = null
    }

    fun resetTimer() {
        pauseTimer()
        _elapsedSec.value = 0
    }

    fun loadCurrentLocation() {
        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()
            _currentLocation.value = location?.let {
                LatLng(it.latitude, it.longitude)

            }
            Log.d(TAG, "$location")

        }
    }

}