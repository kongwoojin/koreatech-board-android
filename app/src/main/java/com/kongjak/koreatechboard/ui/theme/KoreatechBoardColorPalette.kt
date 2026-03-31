package com.kongjak.koreatechboard.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class KoreatechBoardColorPalette(
    val hyperLink: Color = Color.Unspecified
)

val LocalKoreatechBoardColorPalette = staticCompositionLocalOf { KoreatechBoardColorPalette() }
