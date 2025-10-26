package com.tteoli.home.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tteoli.home.R
import com.tteoli.home.ui.theme.LockRunAppTheme

@Composable
fun HomeScreen() {
    LockRunAppTheme {
        val target = LatLng(35.3350072, 129.0371689)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(target, 17.5f)
        }

        val context = LocalContext.current
        val mapStyleOptions = remember {
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_dark_style)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,           // 스타일 적용은 NORMAL에서만 보장
                    isBuildingEnabled = true,           // 필요 시 false로 비교 테스트
                    isIndoorEnabled = true,             // 필요 시 false로 비교 테스트
                    mapStyleOptions = mapStyleOptions
                )
            ) {

            }

            // === 가운데 투명 + 외곽 어둡게 비네트 오버레이 ===
            RadialGradientOverlay(
                modifier = Modifier.fillMaxSize(),
                innerTransparentFraction = 0.00f,      // 0.0~1.0: 중앙 투명반경 (값 ↑ = 투명영역 확대)
                fadeRadiusFraction = 0.8f,             // 0.0~1.0: 그라데이션이 닿는 전체 반경
                edgeColor = Color(0xFF16192B),         // 외곽 어두운 색 (다크 블루/네이비 톤)
                globalAlpha = 9.5f                    // 전체 알파(불투명도). 0.0~1.0
            )
        }
    }
}

/**
 * 중심은 완전 투명, 외곽으로 갈수록 어두워지는 원형 그라데이션 오버레이.
 * - innerTransparentFraction: 중심부 완전 투명 영역의 비율
 * - fadeRadiusFraction: 전체 캔버스의 최단 변 기준으로 그라데이션 반경 비율
 * - edgeColor: 외곽 암색
 * - globalAlpha: 전체 오버레이 투명도
 */
@Composable
private fun RadialGradientOverlay(
    modifier: Modifier = Modifier,
    innerTransparentFraction: Float = 0.0f,
    fadeRadiusFraction: Float = 0.6f,
    edgeColor: Color = Color(0xFF0A0F23),
    globalAlpha: Float = 1.0f
) {
    Canvas(modifier = modifier) {
        // 반경 계산 (화면의 짧은 변을 기준으로 안정적 동작)
        val minDim = size.minDimension
        val center: Offset = this.center

        // 그라데이션이 닿는 최종 반경
        val outerRadius = minDim * fadeRadiusFraction.coerceIn(0.0f, 1.0f)
        // 중앙 투명반경
        val innerStop = innerTransparentFraction.coerceIn(0.0f, 0.99f)

        // colorStops: 0.0~innerStop까지는 완전 투명, 이후 outer까지 어두워짐
        val stops = arrayOf(
            0.1f to Color.Transparent,
            innerStop to Color.Transparent,
            1.0f to edgeColor
        )

        drawRect(
            brush = Brush.radialGradient(
                colorStops = stops,
                center = center,
                radius = outerRadius
            ),
            alpha = globalAlpha
        )
    }
}
