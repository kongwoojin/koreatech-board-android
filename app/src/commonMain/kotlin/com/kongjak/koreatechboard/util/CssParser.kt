package com.kongjak.koreatechboard.util

import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import io.github.aakira.napier.Napier

fun parseColor(value: String, isDarkMode: Boolean, isBackground: Boolean = false): Color {
    return try {
        var red: Int
        var green: Int
        var blue: Int
        if (value.startsWith("#")) {
            red = Integer.parseInt(value.substring(1, 3), 16)
            green = Integer.parseInt(value.substring(3, 5), 16)
            blue = Integer.parseInt(value.substring(5, 7), 16)
        } else if (value.startsWith("rgb")) {
            if (value.split("rgb").size > 2) return Color.Unspecified
            val tmp = parseRGB(value)
            red = tmp[0]
            green = tmp[1]
            blue = tmp[2]
        } else {
            val color = when (value) {
                "black" -> Color.Black
                "white" -> Color.White
                "red" -> Color.Red
                "green" -> Color.Green
                "blue" -> Color.Blue
                "yellow" -> Color.Yellow
                "cyan" -> Color.Cyan
                "magenta" -> Color.Magenta
                "gray" -> Color.Gray
                "lightgray" -> Color.LightGray
                "darkgray" -> Color.DarkGray
                "grey" -> Color.Gray
                "lightgrey" -> Color.LightGray
                "darkgrey" -> Color.DarkGray
                else -> Color.Unspecified
            }

            red = color.red.toInt()
            green = color.green.toInt()
            blue = color.blue.toInt()
        }

        if (isDarkMode) {
            val luminance = 0.299 * red + 0.587 * green + 0.114 * blue
            if (!isBackground) {
                if (luminance <= 186) {
                    red = 255 - red
                    green = 255 - green
                    blue = 255 - blue
                }
            } else {
                if (luminance > 186) {
                    red = 255 - red
                    green = 255 - green
                    blue = 255 - blue
                }
            }
        }
        return Color(red, green, blue)
    } catch (e: Exception) {
        Napier.e("Error parsing color: $value", e)
        Color.Unspecified
    }
}

private fun parseFontWeight(value: String): FontWeight? {
    return if (value.isNumber()) {
        FontWeight(Integer.parseInt(value))
    } else {
        when (value) {
            "normal" -> FontWeight.Normal
            "bold" -> FontWeight.Bold
            "bolder" -> FontWeight.ExtraBold
            "lighter" -> FontWeight.Light
            else -> null
        }
    }
}

private fun parseTextDecoration(value: String): TextDecoration {
    return when (value) {
        "line-through" -> TextDecoration.LineThrough
        "underline" -> TextDecoration.Underline
        else -> TextDecoration.None
    }
}

private fun parseRGB(rgb: String): IntArray {
    return rgb
        .replace("rgb", "")
        .replace("(", "")
        .replace(")", "")
        .replace(";", "")
        .split(",").map { Integer.parseInt(it.trim()) }.toIntArray()
}

private fun String.isNumber(): Boolean {
    return when (this.toIntOrNull()) {
        null -> false
        else -> true
    }
}

fun parseRawStyle(css: String?, isDarkMode: Boolean): String {
    if (css.isNullOrEmpty()) return ""
    val cssMap = css.split(";").filter { it.isNotBlank() }.associate {
        val (key, value) = it.split(":")
        key.trim() to value.trim()
    }.toMutableMap()

    for (colorKey in listOf("color", "border-color")) {
        if (cssMap.containsKey(colorKey)) {
            val color = parseColor(cssMap[colorKey]!!, isDarkMode)
            if (color == Color.Unspecified) {
                cssMap[colorKey] = if (isDarkMode) "white;" else "black;"
            } else {
                cssMap[colorKey] = color.toRaw()
            }
        } else {
            cssMap[colorKey] = if (isDarkMode) "white;" else "black;"
        }
    }

    for (backgroundKey in listOf("background", "background-color")) {
        if (cssMap.containsKey(backgroundKey)) {
            val color = parseColor(cssMap[backgroundKey]!!, isDarkMode, true)
            if (color == Color.Unspecified) {
                cssMap[backgroundKey] = ""
            } else {
                cssMap[backgroundKey] = color.toRaw()
            }
        }
    }

    return cssMap.map { (key, value) -> "$key: $value;" }.joinToString(" ")
}

fun parseSpanStyle(css: String?, isDarkMode: Boolean): SpanStyle {
    if (css == null) return SpanStyle()
    if (css.isEmpty()) return SpanStyle()
    val cssMap = css.split(";").filter { it.isNotBlank() }.associate {
        try {
            val (key, value) = it.split(":")
            key.trim() to value.trim()
        } catch (e: Exception) {
            Napier.e("Error parsing css: $css")
            "" to ""
        }
    }

    val hasBackgroundColor = cssMap.containsKey("background-color")

    var color = Color.Unspecified
    var fontWeight: FontWeight? = null
    var textDecoration = TextDecoration.None

    for ((key, value) in cssMap) {
        when (key) {
            "color" -> {
                /*
                * If the background color is set, don't parse color.
                 */
                if (!hasBackgroundColor) {
                    color = parseColor(value, isDarkMode)
                }
            }

            "font-weight" -> {
                fontWeight = parseFontWeight(value)
            }

            "text-decoration" -> {
                textDecoration = parseTextDecoration(value)
            }
        }
    }

    return SpanStyle(
        color = color,
        fontWeight = fontWeight,
        textDecoration = textDecoration
    )
}

fun Color.toRaw(): String {
    return "rgb(${this.toArgb().red}, ${this.toArgb().green}, ${this.toArgb().blue})"
}

inline val @receiver:ColorInt Int.red: Int get() = (this shr 16) and 0xff
inline val @receiver:ColorInt Int.green: Int get() = (this shr 8) and 0xff
inline val @receiver:ColorInt Int.blue: Int get() = this and 0xff
