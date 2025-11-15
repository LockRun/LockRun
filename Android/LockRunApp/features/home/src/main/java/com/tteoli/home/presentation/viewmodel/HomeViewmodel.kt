package com.tteoli.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tteoli.core.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeViewmodel"
@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

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