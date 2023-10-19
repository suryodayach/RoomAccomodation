package com.suryodayach.core.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.suryodayach.core.ui.R.array.com_google_android_gms_fonts_certs

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = com_google_android_gms_fonts_certs
)

val NeutonFont = GoogleFont(name = "Neuton")

val NeutonFontFamily = FontFamily(
    Font(googleFont = NeutonFont, fontProvider = provider),
    Font(googleFont = NeutonFont, fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = NeutonFont, fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = NeutonFont, fontProvider = provider, weight = FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
//    displayLarge = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Light,
//        fontSize = 57.sp,
//        lineHeight = 64.sp,
//        letterSpacing = 0.sp
//    ),
//    displayMedium = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Light,
//        fontSize = 45.sp,
//        lineHeight = 52.sp,
//        letterSpacing = 0.sp
//    ),
//    displaySmall = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 36.sp,
//        lineHeight = 44.sp,
//        letterSpacing = 0.sp
//    ),
//    headlineLarge = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 32.sp,
//        lineHeight = 40.sp,
//        letterSpacing = 0.sp
//    ),
//    headlineMedium = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 28.sp,
//        lineHeight = 36.sp,
//        letterSpacing = 0.sp
//    ),
//    headlineSmall = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 24.sp,
//        lineHeight = 32.sp,
//        letterSpacing = 0.sp
//    ),
    titleLarge = TextStyle(
        fontFamily = NeutonFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NeutonFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
//    titleSmall = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Bold,
//        fontSize = 14.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 0.1.sp
//    ),
//    bodyLarge = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.15.sp
//    ),
//    bodyMedium = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Medium,
//        fontSize = 14.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 0.25.sp
//    ),
//    bodySmall = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.Bold,
//        fontSize = 12.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.4.sp
//    ),
//    labelLarge = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 14.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 0.1.sp
//    ),
//    labelMedium = TextStyle(
//        fontFamily = NeutonFontFamily,
//        fontWeight = FontWeight.SemiBold,
//        fontSize = 12.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)
