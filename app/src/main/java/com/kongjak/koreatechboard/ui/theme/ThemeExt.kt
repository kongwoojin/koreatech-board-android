package com.kongjak.koreatechboard.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/*
 * Fixed dynamic color scheme
 * Issue: https://github.com/material-components/material-components-android/issues/3924
 * Based on https://github.com/material-components/material-components-android/blob/master/docs/theming/Color.md
 */

@RequiresApi(Build.VERSION_CODES.S)
fun fixedDynamicLightColorScheme(
    context: Context
) = lightColorScheme(
    primary = getColor(context, android.R.color.system_accent1_600),
    onPrimary = getColor(context, android.R.color.system_accent1_0),
    primaryContainer = getColor(context, android.R.color.system_accent1_100),
    onPrimaryContainer = getColor(context, android.R.color.system_accent1_900),
    inversePrimary = getColor(context, android.R.color.system_accent1_200),
    secondary = getColor(context, android.R.color.system_accent2_600),
    onSecondary = getColor(context, android.R.color.system_accent2_0),
    secondaryContainer = getColor(context, android.R.color.system_accent2_100),
    onSecondaryContainer = getColor(context, android.R.color.system_accent2_900),
    tertiary = getColor(context, android.R.color.system_accent3_600),
    onTertiary = getColor(context, android.R.color.system_accent3_0),
    tertiaryContainer = getColor(context, android.R.color.system_accent3_100),
    onTertiaryContainer = getColor(context, android.R.color.system_accent3_900),
    onBackground = getColor(context, android.R.color.system_neutral1_900),
    onSurface = getColor(context, android.R.color.system_neutral1_900),
    surfaceVariant = getColor(context, android.R.color.system_neutral2_100),
    onSurfaceVariant = getColor(context, android.R.color.system_neutral2_700),
    inverseSurface = getColor(context, android.R.color.system_neutral1_800),
    inverseOnSurface = getColor(context, android.R.color.system_neutral1_50),
    outline = getColor(context, android.R.color.system_neutral2_500),
    outlineVariant = getColor(context, android.R.color.system_neutral2_200),
    surfaceContainerLowest = getColor(context, android.R.color.system_neutral2_0),
    surfaceContainerHighest = getColor(context, android.R.color.system_neutral2_100)
)

@RequiresApi(Build.VERSION_CODES.S)
fun fixedDynamicDarkColorScheme(
    context: Context
) = darkColorScheme(
    primary = getColor(context, android.R.color.system_accent1_200),
    onPrimary = getColor(context, android.R.color.system_accent1_800),
    primaryContainer = getColor(context, android.R.color.system_accent1_700),
    onPrimaryContainer = getColor(context, android.R.color.system_accent1_100),
    inversePrimary = getColor(context, android.R.color.system_accent1_600),
    secondary = getColor(context, android.R.color.system_accent2_200),
    onSecondary = getColor(context, android.R.color.system_accent2_800),
    secondaryContainer = getColor(context, android.R.color.system_accent2_700),
    onSecondaryContainer = getColor(context, android.R.color.system_accent2_100),
    tertiary = getColor(context, android.R.color.system_accent3_200),
    onTertiary = getColor(context, android.R.color.system_accent3_800),
    tertiaryContainer = getColor(context, android.R.color.system_accent3_700),
    onTertiaryContainer = getColor(context, android.R.color.system_accent3_100),
    onBackground = getColor(context, android.R.color.system_neutral1_100),
    onSurface = getColor(context, android.R.color.system_neutral1_100),
    surfaceVariant = getColor(context, android.R.color.system_neutral2_700),
    onSurfaceVariant = getColor(context, android.R.color.system_neutral2_200),
    inverseSurface = getColor(context, android.R.color.system_neutral1_100),
    inverseOnSurface = getColor(context, android.R.color.system_neutral1_800),
    outline = getColor(context, android.R.color.system_neutral2_400),
    outlineVariant = getColor(context, android.R.color.system_neutral2_700),
    surfaceContainerLow = getColor(context, android.R.color.system_neutral2_900)
)

internal fun getColor(context: Context, resId: Int): Color {
    return Color(context.getColor(resId))
}
