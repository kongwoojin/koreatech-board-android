package com.kongjak.koreatechboard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val Typography.preferenceColumnTitle: TextStyle
    @Composable
    get() = TextStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.preferenceTitle: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.preferenceSummary: TextStyle
    get() = TextStyle(
        color = Color(0xFF808080),
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.articleTitle: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.articleSubText: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 4.sp,
        letterSpacing = 0.5.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.boardItemTitle: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.boardItemSubText: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 4.sp,
        letterSpacing = 0.5.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

val Typography.boardItemAboveText: TextStyle
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )
