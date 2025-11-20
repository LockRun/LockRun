package com.tteoli.ui_components.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tteoli.ui_components.R

private val pretendardThin = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin)
)
private val pretendardRegular = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal)
)
private val pretendardBold = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold)
)
private val pretendardExtraBold = FontFamily(
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold)
)
private val pretendardSemiBold = FontFamily(
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold)
)

private val pretendardExtraLight = FontFamily(
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight)
)
private val pretendardLight = FontFamily(
    Font(R.font.pretendard_light, FontWeight.Light)
)
private val pretendardMedium = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Medium)
)

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge= TextStyle(
        fontFamily= pretendardBold,
        fontSize = 42.sp,
    ),
    headlineMedium= TextStyle(
        fontFamily= pretendardBold,
        fontSize = 32.sp,
    ),
    headlineSmall= TextStyle(
        fontFamily= pretendardBold,
        fontSize = 24.sp,
    ),
    titleLarge= TextStyle(
        fontFamily= pretendardBold,
        fontSize = 30.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily= pretendardBold,
        fontSize = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily= pretendardBold,
        fontSize = 14.sp,
    ),
    labelMedium = TextStyle(
        fontFamily= pretendardRegular,
        fontSize = 14.sp,
    ),

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

    val displayLarge: TextStyle = TypographyTokens.DisplayLarge,
    val displayMedium: TextStyle = TypographyTokens.DisplayMedium,
    val displaySmall: TextStyle = TypographyTokens.DisplaySmall,
    val headlineLarge: TextStyle = TypographyTokens.HeadlineLarge,
    val headlineMedium: TextStyle = TypographyTokens.HeadlineMedium,
    val headlineSmall: TextStyle = TypographyTokens.HeadlineSmall,
    val titleLarge: TextStyle = TypographyTokens.TitleLarge,
    val titleMedium: TextStyle = TypographyTokens.TitleMedium,
    val titleSmall: TextStyle = TypographyTokens.TitleSmall,
    val bodyLarge: TextStyle = TypographyTokens.BodyLarge,
    val bodyMedium: TextStyle = TypographyTokens.BodyMedium,
    val bodySmall: TextStyle = TypographyTokens.BodySmall,
    val labelLarge: TextStyle = TypographyTokens.LabelLarge,
    val labelMedium: TextStyle = TypographyTokens.LabelMedium,
    val labelSmall: TextStyle = TypographyTokens.LabelSmall,
    */
)