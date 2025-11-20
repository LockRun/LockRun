package com.tteoli.ui_components.theme.color

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


// Blue
val Blue900 = Color(0xFF394DDE)
val Blue800 = Color(0xFF4E61E2)
val Blue700 = Color(0xFF6475E6)
val Blue600 = Color(0xFF7A88E9)
val Blue500 = Color(0xFF909CED)
val Blue400 = Color(0xFFA6AFF0)
val Blue300 = Color(0xFFBCC3F4)
val Blue200 = Color(0xFFD2D7F8)

// Navy
val Navy900 = Color(0xFF1D1F2F)
val Navy800 = Color(0xFF1F2139)
val Navy700 = Color(0xFF2A2B47)
val Navy600 = Color(0xFF333457)
val Navy500 = Color(0xFF404263)
val Navy400 = Color(0xFF515379)
val Navy300 = Color(0xFF66678F)
val Navy200 = Color(0xFF7C7DA4)

// Gray
val Grey900 = Color(0xFF333333)
val Grey700 = Color(0xFF606060)
val Grey500 = Color(0xFFA1A1A1)
val Grey300 = Color(0xFFBEBEBE)
val Grey200 = Color(0xFFF2F2F2)

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

// Background
val Background1 =Color(0xFF0F1014)
val Background2 =Color(0xFF000000).copy(alpha = 0.50f)
val Background3 =Color(0xFF000000).copy(alpha = 0.70f)
val BackgroundContent1 =Color(0xFF000000).copy(alpha = 0.60f)
val BackgroundContent2 =Color(0xFFD9D9D9).copy(alpha = 0.05f)


sealed class ColorSet {
    open lateinit var LightColorScheme: MyColors
    open lateinit var DarkColorScheme: MyColors

    object DefaultColorSet : ColorSet() {

        override var LightColorScheme: MyColors = MyColors(
            scheme = lightColorScheme(
                primary = Background1,
                primaryContainer = White,
                secondary = Background2,
                secondaryContainer = White,
                surface = White,
                onSurface = Black,
                background = White,
                onBackground = Black,
                onPrimary = White
            ),
            success = Color.Green,
            disabledSecondary = Grey300,
            backgroundVariant = Grey200
        )

        override var DarkColorScheme: MyColors = MyColors(
            scheme = darkColorScheme(
                primary = Background1,
                primaryContainer = White,
                secondary = Background2,
                secondaryContainer = White,
                surface = White,
                onSurface = Black,
                background = White,
                onBackground = Black,
                onPrimary = White
            ),
            success = Color.Green,
            disabledSecondary = Grey300,
            backgroundVariant = Grey200
        )
    }

}

