package com.tteoli.ui_components.theme.color

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

data class MyColors(
    val scheme: ColorScheme,
    val tertiary: Color = scheme.primary,
    val onPrimaryAlt: Color = scheme.onPrimary,
    val success: Color = Color.Green,
    val checked: Color = Color.White,
    val unchecked: Color = Color.White,
    val checkmark: Color = scheme.primary,
    val disabledSecondary: Color = scheme.secondary.copy(alpha = 0.5f),
    val backgroundVariant: Color = Color.LightGray,
    val textFiledBackgroundVariant: Color = Color.DarkGray,
    val launcherScreenBackground: Color = scheme.primary,
    val progressItemColor: Color = Color.Black
) {
    val primary: Color get() = scheme.primary
    val primaryVariant: Color get() = scheme.primaryContainer   // 대체 매핑
    val secondary: Color get() = scheme.secondary
    val secondaryVariant: Color get() = scheme.secondaryContainer
    val background: Color get() = scheme.background
    val surface: Color get() = scheme.surface
    val error: Color get() = scheme.error
    val onPrimary: Color get() = scheme.onPrimary
    val onSecondary: Color get() = scheme.onSecondary
    val onBackground: Color get() = scheme.onBackground
    val onSurface: Color get() = scheme.onSurface
    val onError: Color get() = scheme.onError
}