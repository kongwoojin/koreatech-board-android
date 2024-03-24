package com.kongjak.koreatechboard.ui.components

import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kongjak.koreatechboard.util.HtmlData
import com.kongjak.koreatechboard.util.HtmlTags
import com.kongjak.koreatechboard.util.parseSpanStyle
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

@Composable
fun HtmlView(
    modifier: Modifier = Modifier,
    html: String,
    isDarkMode: Boolean = false,
    image: @Composable (String, String) -> Unit
) {
    val xmlPullParserFactory = XmlPullParserFactory.newInstance()
    val parser = xmlPullParserFactory.newPullParser()
    parser.setInput(html.byteInputStream(), "UTF-8")

    var eventType = parser.eventType

    val queue = ArrayDeque<HtmlData>()

    var tag: HtmlTags? = null
    var attributes: Map<String, String> = emptyMap()
    var text = ""

    while (eventType != XmlPullParser.END_DOCUMENT) {
        when (eventType) {
            XmlPullParser.START_TAG -> {
                if (tag != null) {
                    if (text.isBlank()) {
                        if (queue.size > 0) {
                            queue.add(HtmlData(tag, attributes, text))
                            text = ""
                        }
                    } else {
                        queue.add(HtmlData(tag, attributes, text))
                        text = ""
                    }
                }

                tag = HtmlTags.valueOf(parser.name.uppercase())

                attributes = (0 until parser.attributeCount).associate {
                    parser.getAttributeName(it) to parser.getAttributeValue(it)
                }
            }

            XmlPullParser.TEXT -> {
                text = parser.text
            }

            XmlPullParser.END_TAG -> {
                if (tag != null) {
                    if (text.isBlank()) {
                        if (queue.size > 0) {
                            queue.add(HtmlData(tag, attributes, text))
                            tag = null
                            attributes = emptyMap()
                            text = ""
                        }
                    } else {
                        queue.add(HtmlData(tag, attributes, text))
                        tag = null
                        attributes = emptyMap()
                        text = ""
                    }
                }
            }
        }

        try {
            eventType = parser.next()
        } catch (e: XmlPullParserException) {
            e.message?.let { Log.e("Exception", it) }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.message?.let { Log.e("Exception", it) }
        }
    }

    if (tag != null) {
        queue.add(HtmlData(tag, attributes, text))
    }

    val tmpQueue = ArrayDeque<HtmlData>()
    val urlList = ArrayDeque<String>()

    while (queue.isNotEmpty()) {
        when (queue.first().tag) {
            HtmlTags.IMG -> {
                RenderText(modifier = modifier, queue = tmpQueue, urlList, isDarkMode = isDarkMode)
                tmpQueue.clear()
                image(queue.first().attributes["src"] ?: "", queue.first().attributes["alt"] ?: "")
            }

            HtmlTags.A -> {
                urlList.add(queue.first().attributes["href"] ?: "")
                tmpQueue.add(queue.first())
            }

            else -> {
                tmpQueue.add(queue.first())
            }
        }
        queue.removeFirst()
    }
    RenderText(modifier = modifier, queue = tmpQueue, urlList, isDarkMode = isDarkMode)
}

@Composable
fun RenderText(
    modifier: Modifier,
    queue: ArrayDeque<HtmlData>,
    urlList: ArrayDeque<String>,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        var needHyperLink = false
        var needLineBreak = false
        for (data in queue) {
            withStyle(parseSpanStyle(data.attributes["style"] ?: "", isDarkMode)) {
                if (data.tag == HtmlTags.A) {
                    needHyperLink = true
                } else {
                    if (needLineBreak) {
                        append("\n")
                        needLineBreak = false
                    }
                    if (needHyperLink && data.text.isNotBlank()) {
                        pushStringAnnotation(
                            tag = data.text,
                            annotation = urlList.first().ifBlank { data.text.trim() }
                        )
                        urlList.removeFirst()
                        needHyperLink = false
                    }
                    append(data.text)

                }
            }
            if (data.tag.isBlock || data.tag == HtmlTags.BR) {
                // Only allow two line breaks
                val text = this.toAnnotatedString()

                needLineBreak = if (text.lastIndex > 0 && text[text.lastIndex] == '\n') {
                    if (text.length < 2) continue
                    if (text[text.lastIndex - 1] == '\n') {
                        continue
                    } else {
                        true
                    }
                } else if (text.lastIndex <= 0) {
                    continue
                } else {
                    true
                }
            }
        }
    }
    SelectionContainer {
        CustomClickableText(modifier = modifier, text = annotatedString, onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { url ->
                    val builder = CustomTabsIntent.Builder()
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(context, Uri.parse(url.item))
                }
        })
    }
}
