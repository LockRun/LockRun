package com.tteoli.ui_components.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tteoli.ui_components.theme.color.ColorSet
import com.tteoli.ui_components.theme.color.MyColors
import com.tteoli.ui_components.theme.color.Pink40
import com.tteoli.ui_components.theme.color.Pink80
import com.tteoli.ui_components.theme.color.Purple40
import com.tteoli.ui_components.theme.color.Purple80
import com.tteoli.ui_components.theme.color.PurpleGrey40
import com.tteoli.ui_components.theme.color.PurpleGrey80

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LockRunAppTheme(
    myColors: ColorSet = ColorSet.DefaultColorSet,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {


    val colorScheme: MyColors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            val scheme =
                if (darkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)

            MyColors(scheme = scheme)   // ✅ dynamic ColorScheme를 래핑
        }

        darkTheme -> myColors.DarkColorScheme   // ✅ MyColors
        else -> myColors.LightColorScheme       // ✅ MyColors
    }


    MaterialTheme(
        colorScheme = colorScheme.scheme,
        typography = Typography,
        content = content
    )
}