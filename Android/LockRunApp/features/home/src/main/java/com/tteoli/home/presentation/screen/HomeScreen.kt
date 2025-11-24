package com.tteoli.home.presentation.screen

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import com.tteoli.home.R
import com.tteoli.home.presentation.viewmodel.HomeViewmodel
import com.tteoli.ui_components.PrimaryButton
import com.tteoli.ui_components.theme.LockRunAppTheme

/* ================================
   Constants / Types
   ================================ */

private const val ZOOM_IDLE = 16f
private const val ZOOM_FOCUS = 16.5f
private const val ANIM_MS_SHORT = 800
private const val ANIM_MS_NORMAL = 1000

private val TitleGradient = listOf(Color(0xFFb9e2ff), Color(0xFFc7bfff))

private enum class RunState { Idle, Countdown, Running, Paused, ReCountdown }

/* ================================
   Entry
   ================================ */

@Composable
fun HomeScreen(
    viewModel: HomeViewmodel = hiltViewModel(),
) {

    val timer by viewModel.timer.collectAsState()
    LockRunAppTheme {

        // ----- 상태 수집 -----
        val currentLocation by viewModel.currentLocation.collectAsState()
        val target = currentLocation ?: LatLng(35.3350072, 129.0371689)

        var state by remember { mutableStateOf(RunState.Idle) }

        var myLocationEnabled by remember { mutableStateOf(false) }

        // ----- 지도 관련 상태 -----
        val mapStyle = rememberMapStyle(R.raw.map_dark_style)
        val uiSettings = rememberMapUiSettings()
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(target, ZOOM_IDLE)
        }

        // ----- 위치 권한 요청 + 현재 위치 로딩 -----
        LocationPermissionRequester(
            onGranted = {
                viewModel.loadCurrentLocation()
                myLocationEnabled = true
            }
        )

        // 화면 처음 진입 시 현재 위치 요청
        LaunchedEffect(Unit) {
            viewModel.loadCurrentLocation()
        }

        // 카메라 줌 애니메이션
        LaunchedEffect(state, currentLocation) {
            val zoom = if (state == RunState.Running) ZOOM_FOCUS else ZOOM_IDLE
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(target, zoom, 0f, 0f)
                ),
                durationMs = ANIM_MS_NORMAL
            )
        }

        // 오버레이(원형 그라데이션) 애니메이션 파라미터
        val overlay = rememberOverlayAnimation(state)

        Box(Modifier.fillMaxSize()) {
            // 1) 지도
            MapView(
                uiSettings = uiSettings,
                cameraPositionState = cameraPositionState,
                mapStyleOptions = mapStyle,
                isMyLocationEnabled = myLocationEnabled && state != RunState.Idle
            )

            // 2) 주변 어둡게
            RadialGradientOverlay(
                innerTransparentFraction = overlay.inner,
                fadeRadiusFraction = overlay.fade,
                globalAlpha = overlay.alpha
            )

            // 3) 콘텐츠
            when (state) {
                RunState.Idle -> IdleContent(
                    onStart = { state = RunState.Countdown }
                )

                RunState.Running -> RunningContent(
                    timer,
                    onPause = {
                        viewModel.pauseTimer()
                        state = RunState.Paused
                    }
                )

                RunState.Paused -> PausedContent(
                    timer,
                    onResume = { state = RunState.ReCountdown },
                    onStop = { state = RunState.Idle }
                )

                RunState.Countdown -> IdleContent(
                    onStart = { state = RunState.Countdown }
                )

                RunState.ReCountdown -> PausedContent(
                    timer,
                    onResume = { state = RunState.ReCountdown },
                    onStop = {
                        state = RunState.Idle
                    }
                )
            }

            // 4) 카운트다운
            if (state == RunState.Countdown || state == RunState.ReCountdown) {
                CountdownOverlay(
                    rawRes = R.raw.animation_count,
                    onFinished = {
                        state = RunState.Running
                        viewModel.startTimer()
                    }
                )
            }

        }
    }
}

/* ================================
   Permission
   ================================ */
@Composable
private fun LocationPermissionRequester(
    onGranted: () -> Unit,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fine || coarse) onGranted()
    }

    LaunchedEffect(Unit) {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PERMISSION_GRANTED

        if (fineGranted || coarseGranted) {
            onGranted()
        } else {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

/* ================================
   Remember Helpers
   ================================ */

@Composable
private fun rememberMapStyle(rawRes: Int): MapStyleOptions {
    val ctx = LocalContext.current
    return remember { MapStyleOptions.loadRawResourceStyle(ctx, rawRes) }
}

@Composable
private fun rememberMapUiSettings() = remember {
    MapUiSettings(
        zoomControlsEnabled = false,
        compassEnabled = false,
        myLocationButtonEnabled = false,
        mapToolbarEnabled = false,
        zoomGesturesEnabled = true
    )
}

private data class OverlayAnim(val inner: Float, val fade: Float, val alpha: Float)

@Composable
private fun rememberOverlayAnimation(state: RunState): OverlayAnim {
    val inner by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (state == RunState.Idle || state == RunState.Countdown) 0.40f else 1.0f,
        animationSpec = androidx.compose.animation.core.tween(ANIM_MS_NORMAL),
        label = "inner"
    )
    val fade by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (state == RunState.Idle || state == RunState.Countdown) 0.80f else 1.0f,
        animationSpec = androidx.compose.animation.core.tween(ANIM_MS_NORMAL),
        label = "fade"
    )
    val alpha by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (state == RunState.Idle || state == RunState.Countdown) 9.0f else 0f,
        animationSpec = androidx.compose.animation.core.tween(ANIM_MS_SHORT),
        label = "alpha"
    )
    return OverlayAnim(inner, fade, alpha)
}

/* ================================
   Map
   ================================ */

@Composable
private fun MapView(
    uiSettings: MapUiSettings,
    cameraPositionState: CameraPositionState,
    mapStyleOptions: MapStyleOptions,
    isMyLocationEnabled: Boolean
) {

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL,
            isBuildingEnabled = true,
            isMyLocationEnabled = isMyLocationEnabled,
            isIndoorEnabled = true,
            mapStyleOptions = mapStyleOptions
        ),
        uiSettings = uiSettings
    )
}

/* ================================
   Screens
   ================================ */
// 시작 전 화면
@Composable
private fun IdleContent(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(60.dp))
        // 로고
        GradientTitle(text = "LockRun")
        Spacer(Modifier.size(25.dp))
        // 날씨
        WeatherPill()
        Spacer(Modifier.weight(1f))
        // 러닝 정보
        RunSummaryCard(
            distanceText = "0.0Km/5.0Km",
            rainText = "0%",
            goalTimeText = "20:00 ~ 22:00"
        )
        Spacer(Modifier.size(24.dp))
        // 시작 버튼
        StartButton(onStart)
        Spacer(Modifier.size(80.dp))
    }
}

// 러닝 중 화면
@Composable
private fun RunningContent(timeText: String, onPause: () -> Unit) {

    TimerScaffold(
        stateText = "Running",
        timeText = timeText,
        backgroundAlpha = 0.5f,
        bottomContent = {
            RunProgress(progress = 0.6f, left = "0.8Km", right = "2Km")
            Spacer(Modifier.size(24.dp))
            PauseButton(onPause)
        }, infoContent = {}
    )
}

// 일시정지 화면
@Composable
private fun PausedContent(timeText: String, onResume: () -> Unit, onStop: () -> Unit) {
    TimerScaffold(
        stateText = "Resting",
        timeText = timeText,
        backgroundAlpha = 0.7f,

        bottomContent = {
            RunProgress(progress = 0.6f, left = "0.8Km", right = "2Km")
            Spacer(Modifier.size(24.dp))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                text = "계속 달리기"
            ) { onResume() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp, vertical = 12.dp)
                    .clickable { onStop() },
                contentAlignment = Alignment.Center
            ) {
                Text("여기까지 달리고 싶어요", color = Color(0xFF9B9B9B))
            }
        }, infoContent = {
            Row(horizontalArrangement = Arrangement.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "128",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(4.dp))
                        Text("bpm", fontSize = 18.sp, color = Color(0xFFCCCECF))
                    }
                }
                Spacer(Modifier.size(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_running),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF31D158)),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "5'32'",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(4.dp))
                        Text("/km", fontSize = 18.sp, color = Color(0xFFCCCECF))
                    }
                }
                Spacer(Modifier.size(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFFFED702)),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(Modifier.size(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            "0.00",
                            fontSize = 30.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(4.dp))
                        Text("km", fontSize = 18.sp, color = Color(0xFFCCCECF))
                    }
                }


            }
        }
    )
}

/* ================================
   Building Blocks (shared)
   ================================ */

@Composable
private fun TimerScaffold(
    stateText: String,
    timeText: String,
    backgroundAlpha: Float,

    bottomContent: @Composable ColumnScope.() -> Unit,
    infoContent: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = backgroundAlpha)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(60.dp))
        GlassCardDark {
            Image(
                painter = painterResource(id = R.drawable.ic_running),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.size(8.dp))
            Text(
                stateText,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(Modifier.size(8.dp))

        }

        Spacer(Modifier.size(12.dp))
        GradientTitle(text = timeText, size = 42.sp)
        Spacer(Modifier.size(35.dp))
        infoContent()

        Spacer(Modifier.weight(1f))
        bottomContent()
        Spacer(Modifier.size(80.dp))
    }
}

@Composable
private fun GradientTitle(text: String, size: androidx.compose.ui.unit.TextUnit = 42.sp) {
    Text(
        text,
        style = TextStyle(
            brush = Brush.verticalGradient(colors = TitleGradient),
            fontSize = size,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    )
}

/* ================================
   Cards / Buttons
   ================================ */

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 28.dp,
    color: Color = Color(0xFFD9D9D9).copy(alpha = 0.05f),
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(corner))
            .background(color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
private fun GlassCardDark(
    modifier: Modifier = Modifier,
    corner: Dp = 28.dp,
    content: @Composable RowScope.() -> Unit,
) = GlassCard(modifier, corner, Color(0xFF000000).copy(alpha = 0.60f), content)

@Composable
private fun StartButton(onStart: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .clickable { onStart() },
        corner = 100.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_shoe),
            contentDescription = "러닝 시작",
            modifier = Modifier.size(76.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun PauseButton(onPause: () -> Unit) {
    GlassCardDark(
        modifier = Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .clickable { onPause() },
        corner = 100.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bottle),
            contentDescription = "일시정지",
            modifier = Modifier.size(76.dp),
            contentScale = ContentScale.Fit
        )
    }
}

/* ================================
   Domain UI (Summary / Progress / Weather)
   ================================ */

@Composable
private fun RunSummaryCard(
    distanceText: String,
    rainText: String,
    goalTimeText: String,
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        corner = 22.dp
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_ruler),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(distanceText, color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_progress),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(rainText, color = Color.White, fontSize = 14.sp)
            }
            Spacer(Modifier.height(4.dp))
            Column() {
                Text("러닝 목표", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text(goalTimeText, color = Color(0xFF9B9B9B), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun RunProgress(progress: Float, left: String, right: String) {
    GlassCardDark(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        corner = 22.dp
    ) {
        Column {
            Text("러닝 목표", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LinearDeterminate(progress)
            Row {
                Text(left, color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Text(right, color = Color(0xFF9B9B9B), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun LinearDeterminate(progress: Float) {
    androidx.compose.material3.LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = Color(0xFFCDE4FB),
        trackColor = Color(0x33ffffff),
        gapSize = 0.dp
    )
}

@Composable
private fun WeatherPill(
    tempText: String = "30°C",
    rainProb: String = "30%",
    location: String = "서울",
) {
    GlassCard(modifier = Modifier.wrapContentSize(), corner = 22.dp) {
        WeatherIconText(R.drawable.ic_temperature, tempText)
        Spacer(Modifier.width(18.dp))
        WeatherIconText(R.drawable.ic_rain, rainProb)
        Spacer(Modifier.width(18.dp))
        WeatherIconText(R.drawable.ic_location, location)
    }
}

@Composable
private fun WeatherIconText(iconRes: Int, text: String) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
    Spacer(Modifier.width(6.dp))
    Text(text, color = Color.White, fontSize = 14.sp)
}


/* ================================
   Overlays
   ================================ */

@Composable
private fun CountdownOverlay(rawRes: Int, onFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        speed = 1.0f,
        restartOnPlay = false
    )

    var fired by remember { mutableStateOf(false) }
    LaunchedEffect(progress) {
        if (!fired && progress >= 1f) {
            fired = true
            onFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable {
                onFinished()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(180.dp)
        )
        Text("터치하면 바로 시작됩니다.", color = Color.White)
    }
}

@Composable
private fun RadialGradientOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
    innerTransparentFraction: Float = 0.40f,
    fadeRadiusFraction: Float = 0.8f,
    edgeColor: Color = Color(0xFF16192B),
    globalAlpha: Float = 9.0f,
) {
    Canvas(modifier = modifier) {
        val minDim = size.minDimension
        val center: Offset = this.center
        val outerRadius = minDim * fadeRadiusFraction.coerceIn(0.0f, 1.0f)
        val innerStop = innerTransparentFraction.coerceIn(0.0f, 0.99f)

        val stops = arrayOf(
            0.1f to Color.Transparent,
            innerStop to Color.Transparent,
            1.0f to edgeColor
        )
        drawRect(
            brush = Brush.radialGradient(colorStops = stops, center = center, radius = outerRadius),
            alpha = globalAlpha
        )
    }
}

/* ================================
   Preview
   ================================ */

@Preview(showBackground = true)
@Composable
private fun OverlayPreview() {
    LockRunAppTheme { RadialGradientOverlay() }
}
