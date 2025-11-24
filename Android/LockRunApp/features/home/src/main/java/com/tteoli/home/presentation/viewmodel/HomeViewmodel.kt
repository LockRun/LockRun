package com.tteoli.home.presentation.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tteoli.core.LocationRepository
import com.tteoli.home.presentation.entity.TrackPoint
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

    private val _points = MutableStateFlow<List<TrackPoint>>(emptyList())
    val points: StateFlow<List<TrackPoint>> = _points



    fun onLocationUpdate(location: Location) {
        val now = System.currentTimeMillis()

        val last = _points.value.lastOrNull()
        val speed = if (last != null) {
            val dist = FloatArray(1)
            Location.distanceBetween(
                last.lat, last.lng,
                location.latitude, location.longitude,
                dist
            )
            val dt = (now - last.timeMillis) / 1000f // sec
            if (dt > 0) dist[0] / dt else 0f // m/s
        } else {
            location.speed // OS가 계산한 speed 사용해도 됨
        }

        val newPoint = TrackPoint(
            lat = location.latitude,
            lng = location.longitude,
            timeMillis = now,
            speedMps = speed
        )

        _points.value = _points.value + newPoint
    }

    fun totalDistanceMeters(): Float {
        val list = _points.value
        if (list.size < 2) return 0f
        var sum = 0f
        for (i in 0 until list.lastIndex) {
            val a = list[i]
            val b = list[i + 1]
            val dist = FloatArray(1)
            Location.distanceBetween(a.lat, a.lng, b.lat, b.lng, dist)
            sum += dist[0]
        }
        return sum
    }

    fun startTimer() {
        if (job != null) return // 이미 돌고 있으면 무시

        job = viewModelScope.launch {
            while (isActive) {
                // 1. 현재 위치 가져오기
                val location = locationRepository.getCurrentLocation()
                location?.let { loc ->
                    // 화면에서 쓰는 현재 위치 업데이트
                    _currentLocation.value = LatLng(loc.latitude, loc.longitude)

                    // 트랙 포인트 추가 (속도 계산 포함)
                    onLocationUpdate(loc)
                }

                // 2. 1초 대기
                delay(1000L)

                // 3. 타이머 값 증가 + 문자열 갱신
                _elapsedSec.update { it + 1 }
                _timer.update { formatSecondsToHms(_elapsedSec.value) }
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
        _timer.value = "00:00:00"
        _points.value = emptyList()
        _currentLocation.value = null
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