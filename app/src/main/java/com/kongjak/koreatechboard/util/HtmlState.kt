package com.kongjak.koreatechboard.util

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.kongjak.koreatechboard.constant.HTML_A
import com.kongjak.koreatechboard.constant.HTML_B
import com.kongjak.koreatechboard.constant.HTML_BIG
import com.kongjak.koreatechboard.constant.HTML_BLOCKQUOTE
import com.kongjak.koreatechboard.constant.HTML_BR
import com.kongjak.koreatechboard.constant.HTML_CITE
import com.kongjak.koreatechboard.constant.HTML_COL
import com.kongjak.koreatechboard.constant.HTML_COLGROUP
import com.kongjak.koreatechboard.constant.HTML_DEL
import com.kongjak.koreatechboard.constant.HTML_DFN
import com.kongjak.koreatechboard.constant.HTML_DIV
import com.kongjak.koreatechboard.constant.HTML_EM
import com.kongjak.koreatechboard.constant.HTML_FONT
import com.kongjak.koreatechboard.constant.HTML_H1
import com.kongjak.koreatechboard.constant.HTML_H2
import com.kongjak.koreatechboard.constant.HTML_H3
import com.kongjak.koreatechboard.constant.HTML_H4
import com.kongjak.koreatechboard.constant.HTML_H5
import com.kongjak.koreatechboard.constant.HTML_H6
import com.kongjak.koreatechboard.constant.HTML_I
import com.kongjak.koreatechboard.constant.HTML_IMG
import com.kongjak.koreatechboard.constant.HTML_LI
import com.kongjak.koreatechboard.constant.HTML_P
import com.kongjak.koreatechboard.constant.HTML_S
import com.kongjak.koreatechboard.constant.HTML_SMALL
import com.kongjak.koreatechboard.constant.HTML_SPAN
import com.kongjak.koreatechboard.constant.HTML_STRIKE
import com.kongjak.koreatechboard.constant.HTML_STRONG
import com.kongjak.koreatechboard.constant.HTML_SUB
import com.kongjak.koreatechboard.constant.HTML_SUP
import com.kongjak.koreatechboard.constant.HTML_TABLE
import com.kongjak.koreatechboard.constant.HTML_TD
import com.kongjak.koreatechboard.constant.HTML_TH
import com.kongjak.koreatechboard.constant.HTML_TR
import com.kongjak.koreatechboard.constant.HTML_TT
import com.kongjak.koreatechboard.constant.HTML_U
import com.kongjak.koreatechboard.constant.HTML_UL
import com.kongjak.koreatechboard.constant.REGEX_EMAIL
import com.kongjak.koreatechboard.constant.REGEX_HTTP_HTTPS
import com.kongjak.koreatechboard.constant.REGEX_PHONE_NUMBER
import com.kongjak.koreatechboard.ui.components.ANNOTATION_EMAIL_PREFIX
import com.kongjak.koreatechboard.ui.components.ANNOTATION_PHONE_NUMBER_PREFIX
import com.kongjak.koreatechboard.ui.components.ANNOTATION_URL_PREFIX
import com.kongjak.koreatechboard.ui.components.appendNewLine
import com.kongjak.koreatechboard.ui.theme.koreatechColorPalette
import org.kobjects.ktxml.api.EventType
import org.kobjects.ktxml.api.XmlPullParserException
import org.kobjects.ktxml.mini.MiniXmlPullParser
import kotlin.random.Random

class HtmlState internal constructor(
    val customHtmlViewPosition: List<Int>,
    val customHtmlViewQueue: ArrayDeque<CustomHtmlView>,
    val text: AnnotatedString
)

data class CustomHtmlView(
    val type: CustomHtmlViewType,
    val data: Map<String, Any>
) {
    override fun toString(): String {
        return "CustomHtmlView(type=$type, data=$data)"
    }

    enum class CustomHtmlViewType {
        IMAGE,
        WEB_VIEW
    }
}

@Composable
fun rememberHtmlState(
    baseUrl: String,
    html: String,
    parser: MiniXmlPullParser = MiniXmlPullParser(source = html.iterator(), relaxed = true),
    isDarkTheme: Boolean = false
): HtmlState {
    val customHtmlViewPosition: MutableList<Int> = mutableListOf(0)
    val customHtmlViewQueue = ArrayDeque<CustomHtmlView>()

    var isWebView = false
    val webViewHtml = StringBuilder()

    var eventType = parser.eventType

    val text = buildAnnotatedString {
        while (eventType != EventType.END_DOCUMENT) {
            when (eventType) {
                EventType.START_TAG -> {
                    when (parser.name) {
                        HTML_BR -> {
                            if (isWebView) {
                                webViewHtml.append("<br>")
                            } else {
                                appendNewLine()
                            }
                        }

                        HTML_P -> {
                            if (isWebView) {
                                webViewHtml.append("<p>")
                            } else {
                                appendNewLine()
                            }
                        }

                        HTML_UL -> {
                            append("\u2022")
                        }

                        HTML_LI -> {
                            append("\u2022")
                        }

                        HTML_DIV -> {
                            if (isWebView) {
                                webViewHtml.append("<div>")
                            } else {
                                appendNewLine()
                            }
                        }

                        HTML_SPAN -> {
                            if (isWebView) {
                                parser.getAttributeValue("", "style")?.let { style ->
                                    webViewHtml.append(
                                        "<span style=\"${
                                        parseRawStyle(
                                            style,
                                            isDarkTheme
                                        )
                                        }\">"
                                    )
                                } ?: {
                                    webViewHtml.append("<span>")
                                }
                            } else {
                                val style = parser.getAttributeValue("", "style")
                                pushStyle(parseSpanStyle(style, isDarkTheme))
                            }
                        }

                        HTML_STRONG,
                        HTML_B -> {
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        }

                        HTML_EM,
                        HTML_CITE,
                        HTML_DFN,
                        HTML_I -> {
                            pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        }

                        HTML_BIG -> {
                            pushStyle(SpanStyle(fontSize = 20.sp))
                        }

                        HTML_SMALL -> {
                            pushStyle(SpanStyle(fontSize = 10.sp))
                        }

                        HTML_FONT -> {
                            val size = parser.getAttributeValue("", "size")
                            val color = parser.getAttributeValue("", "color")
                            var style = SpanStyle()
                            if (size != null) {
                                style = style.copy(fontSize = size.toInt().sp)
                            }
                            if (color != null) {
                                style = style.copy(color = parseColor(color, isDarkTheme))
                            }
                            pushStyle(style)
                        }

                        HTML_BLOCKQUOTE -> {}
                        HTML_TT -> {
                            pushStyle(SpanStyle(fontFamily = FontFamily.Monospace))
                        }

                        HTML_A -> {
                            val href = parser.getAttributeValue("", "href")
                            val urlTag = "url_${Random.nextInt(0, 1000)}"
                            pushStringAnnotation(
                                tag = urlTag,
                                annotation = href ?: ""
                            )
                        }

                        HTML_U -> {
                            pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                        }

                        HTML_DEL,
                        HTML_S,
                        HTML_STRIKE -> {
                            pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                        }

                        HTML_SUP -> {
                            pushStyle(
                                SpanStyle(
                                    fontSize = 10.sp,
                                    baselineShift = BaselineShift.Superscript
                                )
                            )
                        }

                        HTML_SUB -> {
                            pushStyle(
                                SpanStyle(
                                    fontSize = 10.sp,
                                    baselineShift = BaselineShift.Subscript
                                )
                            )
                        }

                        HTML_H1 -> {
                            pushStyle(SpanStyle(fontSize = 24.sp))
                            appendNewLine()
                        }

                        HTML_H2 -> {
                            pushStyle(SpanStyle(fontSize = 22.sp))
                            appendNewLine()
                        }

                        HTML_H3 -> {
                            pushStyle(SpanStyle(fontSize = 20.sp))
                            appendNewLine()
                        }

                        HTML_H4 -> {
                            pushStyle(SpanStyle(fontSize = 18.sp))
                            appendNewLine()
                        }

                        HTML_H5 -> {
                            pushStyle(SpanStyle(fontSize = 16.sp))
                            appendNewLine()
                        }

                        HTML_H6 -> {
                            pushStyle(SpanStyle(fontSize = 14.sp))
                            appendNewLine()
                        }

                        HTML_IMG -> {
                            customHtmlViewPosition.add(length)

                            var imageSrc = parser.getAttributeValue("", "src") ?: ""
                            imageSrc.contains("https").let {
                                if (!it) {
                                    imageSrc = "$baseUrl$imageSrc"
                                }
                            }

                            customHtmlViewQueue.add(
                                CustomHtmlView(
                                    type = CustomHtmlView.CustomHtmlViewType.IMAGE,
                                    data = mapOf(
                                        "src" to imageSrc,
                                        "alt" to (parser.getAttributeValue("", "alt") ?: "")
                                    )
                                )
                            )
                        }

                        HTML_TABLE -> {
                            isWebView = true
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<table ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }

                        HTML_TR -> {
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<tr ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }

                        HTML_TD -> {
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<td ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }

                        HTML_TH -> {
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<th ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }

                        HTML_COLGROUP -> {
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<colgroup ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }

                        HTML_COL -> {
                            val attributeMap = mutableMapOf<String, String>()

                            for (i in 0 until parser.attributeCount) {
                                val attributeName = parser.getAttributeName(i)
                                val attributeValue = parser.getAttributeValue("", attributeName)
                                attributeMap[attributeName] = attributeValue ?: ""
                            }

                            attributeMap["style"] =
                                parseRawStyle(attributeMap["style"], isDarkTheme)

                            webViewHtml.append(
                                "<col ${
                                attributeMap.map { (key, value) -> "$key=\"$value\"" }
                                    .joinToString(" ")
                                }>"
                            )
                        }
                    }
                }

                EventType.TEXT -> {
                    val text = parser.text.trimIndent()
                    if (text.isNotBlank()) {
                        if (isWebView) {
                            webViewHtml.append(text)
                        } else {
                            val length = this.length
                            append(text)

                            val urlOffsets = extractAllURLOffsets(text)

                            for (offset in urlOffsets) {
                                val urlTag = "$ANNOTATION_URL_PREFIX${Random.nextInt(0, 1000)}"
                                addStringAnnotation(
                                    tag = urlTag,
                                    annotation = text.substring(offset.first, offset.last),
                                    start = length + offset.first,
                                    end = length + offset.last
                                )
                                addStyle(
                                    SpanStyle(
                                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    length + offset.first,
                                    length + offset.last
                                )
                            }

                            val phoneNumberOffsets = extractAllPhoneNumberOffsets(text)

                            for (offset in phoneNumberOffsets) {
                                val phoneNumberTag =
                                    "$ANNOTATION_PHONE_NUMBER_PREFIX${Random.nextInt(0, 1000)}"
                                addStringAnnotation(
                                    tag = phoneNumberTag,
                                    annotation = text.substring(offset.first, offset.last),
                                    start = length + offset.first,
                                    end = length + offset.last
                                )
                                addStyle(
                                    SpanStyle(
                                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    length + offset.first,
                                    length + offset.last
                                )
                            }
                            val emailOffsets = extractAllEmailOffsets(text)

                            for (offset in emailOffsets) {
                                val emailTag = "$ANNOTATION_EMAIL_PREFIX${Random.nextInt(0, 1000)}"
                                addStringAnnotation(
                                    tag = emailTag,
                                    annotation = text.substring(offset.first, offset.last),
                                    start = length + offset.first,
                                    end = length + offset.last
                                )
                                addStyle(
                                    SpanStyle(
                                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    length + offset.first,
                                    length + offset.last
                                )
                            }
                        }
                    }
                }

                EventType.END_TAG -> {
                    when (parser.name) {
                        HTML_BR -> {}
                        HTML_P -> {
                            if (isWebView) {
                                webViewHtml.append("</p>")
                            } else {
                                appendNewLine()
                            }
                        }

                        HTML_UL -> {}
                        HTML_LI -> {}
                        HTML_DIV -> {
                            if (isWebView) {
                                webViewHtml.append("</div>")
                            } else {
                                appendNewLine()
                            }
                        }

                        HTML_BLOCKQUOTE -> {}
                        HTML_A,
                        HTML_STRONG,
                        HTML_B,
                        HTML_EM,
                        HTML_CITE,
                        HTML_DFN,
                        HTML_I,
                        HTML_BIG,
                        HTML_SMALL,
                        HTML_FONT,
                        HTML_TT,
                        HTML_U,
                        HTML_DEL,
                        HTML_S,
                        HTML_STRIKE,
                        HTML_SUP,
                        HTML_SUB,
                        HTML_H1,
                        HTML_H2,
                        HTML_H3,
                        HTML_H4,
                        HTML_H5,
                        HTML_H6 -> {
                            pop()
                        }

                        HTML_SPAN -> {
                            if (isWebView) {
                                webViewHtml.append("</span>")
                            } else {
                                pop()
                            }
                        }

                        HTML_TABLE -> {
                            webViewHtml.append("</table>")
                            customHtmlViewPosition.add(length)
                            customHtmlViewQueue.add(
                                CustomHtmlView(
                                    type = CustomHtmlView.CustomHtmlViewType.WEB_VIEW,
                                    data = mapOf(
                                        "html" to webViewHtml.toString()
                                    )
                                )
                            )
                            isWebView = false
                            webViewHtml.setLength(0)
                        }

                        HTML_TR -> {
                            webViewHtml.append("</tr>")
                        }

                        HTML_TD -> {
                            webViewHtml.append("</td>")
                        }

                        HTML_TH -> {
                            webViewHtml.append("</th>")
                        }

                        HTML_COLGROUP -> {
                            webViewHtml.append("</colgroup>")
                        }

                        HTML_COL -> {
                            webViewHtml.append("</col>")
                        }
                    }
                }

                else -> {}
            }

            try {
                eventType = parser.next()
            } catch (e: XmlPullParserException) {
                e.message?.let { Log.e("Exception", it) }
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.message?.let { Log.e("Exception", it) }
            }
        }
    }

    return HtmlState(
        customHtmlViewPosition = customHtmlViewPosition,
        customHtmlViewQueue = customHtmlViewQueue,
        text = text
    )
}

private fun extractAllURLOffsets(text: String): List<IntRange> {
    val urlRegex = Regex(REGEX_HTTP_HTTPS)
    val matches = urlRegex.findAll(text)
    return matches.map { it.range.first..it.range.last + 1 }.toList()
}

private fun extractAllPhoneNumberOffsets(text: String): List<IntRange> {
    val phoneNumberRegex = Regex(REGEX_PHONE_NUMBER)
    val matches = phoneNumberRegex.findAll(text)
    return matches.map { it.range.first..it.range.last + 1 }.toList()
}

private fun extractAllEmailOffsets(text: String): List<IntRange> {
    val emailRegex = Regex(REGEX_EMAIL)
    val matches = emailRegex.findAll(text)
    return matches.map { it.range.first..it.range.last + 1 }.toList()
}
