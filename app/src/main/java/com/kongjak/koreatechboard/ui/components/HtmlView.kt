package com.kongjak.koreatechboard.ui.components

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.kongjak.koreatechboard.ui.components.text.CustomClickableText
import com.kongjak.koreatechboard.util.CustomHtmlView
import com.kongjak.koreatechboard.util.HtmlState

@Composable
fun HtmlView(
    htmlState: HtmlState,
    modifier: Modifier = Modifier,
    image: @Composable (String, String) -> Unit,
    webView: @Composable (String) -> Unit
) {
    var pos = 0
    while (htmlState.customHtmlViewQueue.isNotEmpty()) {
        HtmlText(
            modifier = modifier,
            text = htmlState.text.subSequence(htmlState.customHtmlViewPosition[pos], htmlState.customHtmlViewPosition[pos + 1])
        )
        pos++

        RenderCustomView(
            htmlState.customHtmlViewQueue.removeFirst(),
            image = image,
            webView = webView
        )
    }

    HtmlText(modifier = modifier, text = htmlState.text.subSequence(htmlState.customHtmlViewPosition[pos], htmlState.text.length))
}

@Composable
private fun HtmlText(
    modifier: Modifier = Modifier,
    text: AnnotatedString
) {
    val context = LocalContext.current
    CustomClickableText(
        modifier = modifier,
        text = text,
        onClick = { offset ->
            text.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { url ->
                    if (url.item.isNotBlank()) {
                        if (url.tag.startsWith(ANNOTATION_PHONE_NUMBER_PREFIX)) {
                            val phoneNumber = url.item
                            val intent = Uri.parse("tel:$phoneNumber")
                            context.startActivity(Intent(Intent.ACTION_DIAL, intent))
                        } else if (url.tag.startsWith(ANNOTATION_URL_PREFIX)) {
                            val builder = CustomTabsIntent.Builder()
                            val customTabsIntent = builder.build()
                            customTabsIntent.launchUrl(context, Uri.parse(url.item))
                        } else if (url.tag.startsWith(ANNOTATION_EMAIL_PREFIX)) {
                            val email = url.item
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:$email")
                            context.startActivity(intent)
                        }
                    }
                }
        }
    )
}

@Composable
fun RenderCustomView(
    customHtmlView: CustomHtmlView,
    image: @Composable (String, String) -> Unit,
    webView: @Composable (String) -> Unit
) {
    when (customHtmlView.type) {
        CustomHtmlView.CustomHtmlViewType.IMAGE -> {
            val src = customHtmlView.data["src"] as String
            val alt = customHtmlView.data["alt"] as String
            image(
                src,
                alt
            )
        }

        CustomHtmlView.CustomHtmlViewType.WEB_VIEW -> {
            webView(customHtmlView.data["html"] as String)
        }
    }
}

fun AnnotatedString.Builder.appendNewLine() {
    val len = this.length
    if (len > 0 && this.toAnnotatedString().text[len - 1] != '\n') {
        append("\n\n")
    }
}

const val ANNOTATION_URL_PREFIX = "url_"
const val ANNOTATION_PHONE_NUMBER_PREFIX = "phone_number_"
const val ANNOTATION_EMAIL_PREFIX = "email_"
