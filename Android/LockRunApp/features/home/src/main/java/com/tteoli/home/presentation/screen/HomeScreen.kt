package com.tteoli.home.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tteoli.home.R
import com.tteoli.home.ui.theme.LockRunAppTheme

@Composable
fun HomeScreen() {

    var isOn by remember { mutableStateOf(false) }

    LockRunAppTheme {

        // 구글맵에대한 정보
        // todo 사용자 위치를 받아오는거 구현하면 변경
        val target = LatLng(35.3350072, 129.0371689)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(target, 15.5f)
        }
        LaunchedEffect(isOn) {
            if (isOn) {
                // 서서히 줌인 (15.5f → 17f)
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(target, 16.5f, 0f, 0f)
                    ),
                    durationMs = 1000 // 2초 동안 서서히 이동
                )
            } else {
                // 원래 줌으로 복귀
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(target, 16f, 0f, 0f)
                    ),
                    durationMs = 1000
                )
            }
        }

        val context = LocalContext.current
        val mapStyleOptions = remember {
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_dark_style)
        }

        val uiSettings = remember {
            MapUiSettings(
                zoomControlsEnabled = false,   // 확대/축소 버튼 끄기
                compassEnabled = false,        // (선택) 나침반 끄기
                myLocationButtonEnabled = false, // (선택) 내 위치 버튼 끄기
                mapToolbarEnabled = false,     // (선택) 길찾기/공유 툴바 끄기
                zoomGesturesEnabled = true     // 핀치 줌은 유지 (막으려면 false)
            )
        }

        // 🔹 3가지 애니메이션 값 (0.4→1.0, 0.8→1.0, 1.0→0.0)
        val innerFraction by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (isOn) 1.0f else 0.4f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1000
            ),
            label = "innerFraction"
        )

        val fadeFraction by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (isOn) 1.0f else 0.80f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 1000
            ),
            label = "fadeFraction"
        )

        val overlayAlpha by androidx.compose.animation.core.animateFloatAsState(
            targetValue = if (isOn) 0f else 9.0f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 800
            ),
            label = "overlayAlpha"
        )



        Box(modifier = Modifier.fillMaxSize()) {

            // 구글 맵
            MapView(uiSettings = uiSettings,cameraPositionState, mapStyleOptions)

            // 그라데이션으로 구글맵 주변 어둡게 설정되는 뷰
            RadialGradientOverlay(
                innerTransparentFraction=innerFraction,
                fadeRadiusFraction= fadeFraction,
                globalAlpha= overlayAlpha)


            // 주요 내용을 보여주는 뷰
            if(!isOn){
                ContentView(onToggle = { isOn = !isOn })
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(Modifier.size(60.dp))
                    GlassCardDark(){
                        Text(
                            "러닝 목표 달성까지 남은 시간",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )

                        )
                    }

                    Spacer(Modifier.size(12.dp))
                    Text(
                        "01:59:58",
                        style = TextStyle(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFb9e2ff), // 위쪽 – 연한 하늘색
                                    Color(0xFFc7bfff)  // 아래쪽 – 보랏빛 파스텔 톤
                                )
                            ),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                    )




                    // 밑에 뷰들을 아래로 붙이기 위한 공간
                    Spacer(modifier = Modifier.weight(1f))

                    // 오늘 뛴 정보
                    RunProgress()
                    Spacer(Modifier.size(24.dp))

                    // 시작 버튼
                    RunBtnDark(onToggle = { isOn = !isOn })
                    Spacer(Modifier.size(80.dp))
                }
            }

        }
    }
}

// 구글맵 뷰
@Composable
fun MapView(
    uiSettings: MapUiSettings,
    cameraPositionState: CameraPositionState,
    mapStyleOptions: MapStyleOptions,
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL,           // 스타일 적용은 NORMAL에서만 보장
            isBuildingEnabled = true,           // 필요 시 false로 비교 테스트
            isIndoorEnabled = true,             // 필요 시 false로 비교 테스트
            mapStyleOptions = mapStyleOptions
        ),
        uiSettings = uiSettings
    )
}

// 주요 내용을 보여주는 뷰
/**
 * 로고, 날씨정보, 뛴 정보, 시작버튼 순으로 정리 되어있음
 */
@Composable
fun ContentView(onToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(60.dp))
        Text(
            "LockRun",
            style = TextStyle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFb9e2ff), // 위쪽 – 연한 하늘색
                        Color(0xFFc7bfff)  // 아래쪽 – 보랏빛 파스텔 톤
                    )
                ),
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

        )

        Spacer(Modifier.size(25.dp))

        // 날씨정보
        WeatherPill()

        // 밑에 뷰들을 아래로 붙이기 위한 공간
        Spacer(modifier = Modifier.weight(1f))

        // 오늘 뛴 정보
        RunPill()
        Spacer(Modifier.size(24.dp))

        // 시작 버튼
        RunBtn(onToggle)
        Spacer(Modifier.size(80.dp))
    }
}


// 커스텀 카드뷰
/**
 * corner 인자를 사용해서 카드뷰 모서리를 라운드 줄 수 있고 기본값으로 28.dp .
 * - {} 안에 뷰를 차가 할수 있고 기본적으로 가로 정렬이 된다.
 * - 배경 색상은 (Color.White.copy(alpha = 0.12f))이고 흰색에서 투명도 12%준 거임
 * - 외과 여백은 세로 18.dp, 가로 14.dp
 */
@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 28.dp,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(corner), clip = false)
            .clip(RoundedCornerShape(corner)),
        color = Color(0xFFD9D9D9).copy(alpha = 0.12f) ,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

// 커스텀 카드뷰
/**
 * corner 인자를 사용해서 카드뷰 모서리를 라운드 줄 수 있고 기본값으로 28.dp .
 * - {} 안에 뷰를 차가 할수 있고 기본적으로 가로 정렬이 된다.
 * - 배경 색상은 (Color.White.copy(alpha = 0.12f))이고 흰색에서 투명도 12%준 거임
 * - 외과 여백은 세로 18.dp, 가로 14.dp
 */
@Composable
private fun GlassCardDark(
    modifier: Modifier = Modifier,
    corner: Dp = 28.dp,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(corner), clip = false)
            .clip(RoundedCornerShape(corner)),
        color = Color(0xFF000000).copy(alpha = 0.30f) ,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

// 오늘 띈 정보를 보여주는 뷰
@Composable
private fun RunPill(){
    GlassCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        corner = 22.dp
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 온도
                Image(painter = painterResource(id = R.drawable.ic_ruler),contentDescription = "러닝 아이콘",
                    modifier = Modifier.size(18.dp),
                    contentScale = ContentScale.Fit)
                Spacer(Modifier.width(6.dp))
                Text("0.0Km/5.0Km", color = Color.White, fontSize = 14.sp)
                Spacer(Modifier.weight(1f))

                // 강수확률
                Image(painter = painterResource(id = R.drawable.ic_progress),contentDescription = "러닝 아이콘",
                    modifier = Modifier.size(18.dp),
                    contentScale = ContentScale.Fit)
                Spacer(Modifier.width(6.dp))
                Text("0%", color = Color.White, fontSize = 14.sp)

            }
            Spacer(Modifier.height(4.dp))
            Column {
//                Image(painter = painterResource(id = R.drawable.ic_time),contentDescription = "러닝 아이콘",
//                    modifier = Modifier.size(24.dp),
//                    contentScale = ContentScale.Fit)
                Text("러닝 목표", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(6.dp))
                Text("20:00 ~ 22:00", color = Color(0xFF9B9B9B), fontSize = 14.sp)
            }

        }
    }
}

@Composable
private fun RunProgress(){
    GlassCardDark(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        corner = 22.dp
    ) {
        Column {
            Column {
//                Image(painter = painterResource(id = R.drawable.ic_time),contentDescription = "러닝 아이콘",
//                    modifier = Modifier.size(24.dp),
//                    contentScale = ContentScale.Fit)


                Text("러닝 목표", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                LinearDeterminateDemo(0.6f)
                Row {
                    Text("0.8Km", color = Color(0xFFFFFFFF), fontSize = 14.sp)
                    Spacer(Modifier.weight(1f))
                    Text("2Km", color = Color(0xFF9B9B9B), fontSize = 14.sp)
                }

            }

        }
    }
}

@Composable
fun LinearDeterminateDemo(progress: Float) {
    // progress: 0f..1f
    androidx.compose.material3.LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = Color(0xFF7BC9FF),     // 진행 색
        trackColor = Color(0x33ffffff), // 트랙(배경) 색 (20% 투명도 예시)
        gapSize= 0.dp

    )
}

// 시작 버튼
@Composable
private fun RunBtn(onToggle: () -> Unit){
    GlassCard(
        modifier = Modifier.wrapContentSize().clip(CircleShape).clickable(){
            onToggle()
        },
        corner = 100.dp
    ) {
        Image(painter = painterResource(id = R.drawable.ic_shoe),contentDescription = "러닝 아이콘",
            modifier = Modifier.size(76.dp),
            contentScale = ContentScale.Fit)
    }
}

@Composable
private fun RunBtnDark(onToggle: () -> Unit){
    GlassCardDark (
        modifier = Modifier.wrapContentSize().clip(CircleShape).clickable(){
            onToggle()
        },
        corner = 100.dp
    ) {
        Image(painter = painterResource(id = R.drawable.ic_shoe),contentDescription = "러닝 아이콘",
            modifier = Modifier.size(76.dp),
            contentScale = ContentScale.Fit)
    }
}

// 날씨 보여주는 뷰
@Composable
private fun WeatherPill(
    tempText: String = "30°C",
    rainProb: String = "30%",
    location: String = "서울"
) {
    GlassCard(
        modifier = Modifier.wrapContentSize(),
        corner = 22.dp
    ) {
        // 온도
        Image(painter = painterResource(id = R.drawable.ic_temperature),contentDescription = "러닝 아이콘",
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit)
        Spacer(Modifier.width(6.dp))
        Text(tempText, color = Color.White, fontSize = 14.sp)
        Spacer(Modifier.width(18.dp))

        // 강수확률
        Image(painter = painterResource(id = R.drawable.ic_rain),contentDescription = "러닝 아이콘",
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit)
        Spacer(Modifier.width(6.dp))
        Text(rainProb, color = Color.White, fontSize = 14.sp)
        Spacer(Modifier.width(18.dp))

        // 위치
        Image(painter = painterResource(id = R.drawable.ic_location),contentDescription = "러닝 아이콘",
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit)
        Spacer(Modifier.width(6.dp))
        Text(location, color = Color.White, fontSize = 14.sp)
    }
}


// 그라데이션으로 구글맵 주변 어둡게 설정되는 뷰
/**
 * 중심은 완전 투명, 외곽으로 갈수록 어두워지는 원형 그라데이션 오버레이.
 * - innerTransparentFraction: 중심부 완전 투명 영역의 비율
 * - fadeRadiusFraction: 전체 캔버스의 최단 변 기준으로 그라데이션 반경 비율
 * - edgeColor: 외곽 암색
 * - globalAlpha: 전체 오버레이 투명도
 */
@Composable
private fun RadialGradientOverlay(
    modifier: Modifier = Modifier.fillMaxSize(),
    innerTransparentFraction: Float =0.40f,
    fadeRadiusFraction: Float = 0.8f,
    edgeColor: Color = Color(0xFF16192B),
    globalAlpha: Float = 9.0f
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
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LockRunAppTheme {
        RadialGradientOverlay()
    }
}