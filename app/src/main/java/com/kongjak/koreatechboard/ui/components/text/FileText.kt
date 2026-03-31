package com.kongjak.koreatechboard.ui.components.text

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.kongjak.koreatechboard.domain.model.Files

@Composable
fun FileText(modifier: Modifier = Modifier, files: List<Files>) {
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        for (file in files) {
            pushStringAnnotation(file.fileName, file.fileUrl)
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(file.fileName)
            }
            pop()
            append("\n")
        }
    }

    ClickableText(modifier = modifier, text = annotatedString) { offset ->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.let { url ->
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(
                    context,
                    if (url.item.startsWith("http")) {
                        Uri.parse(url.item)
                    } else {
                        Uri.parse("http://www.koreatech.ac.kr/${url.item}")
                    }
                )
            }
    }
}
