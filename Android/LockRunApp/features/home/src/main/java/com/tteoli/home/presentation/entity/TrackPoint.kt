package com.tteoli.home.presentation.entity

data class TrackPoint(
    val lat: Double,
    val lng: Double,
    val timeMillis: Long,
    val speedMps: Float,
)
