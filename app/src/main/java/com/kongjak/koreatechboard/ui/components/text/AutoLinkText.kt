package com.kongjak.koreatechboard.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.kongjak.koreatechboard.constant.REGEX_EMAIL
import com.kongjak.koreatechboard.constant.REGEX_HTTP_HTTPS
import com.kongjak.koreatechboard.constant.REGEX_PHONE_NUMBER
import com.kongjak.koreatechboard.ui.theme.koreatechColorPalette

@Composable
fun AutoLinkText(
    text: String,
    modifier: Modifier = Modifier,
    vararg autoLinkType: AutoLinkType = arrayOf(AutoLinkType.ALL),
    openWeb: ((webUrl: String) -> Unit?)? = null,
    openPhone: ((phoneNumber: String) -> Unit?)? = null,
    openEmail: ((email: String) -> Unit?)? = null
) {
    if (autoLinkType.isEmpty()) throw IllegalArgumentException("AutoLinkType must not be empty")

    var web = false
    var phone = false
    var email = false

    var webOffsets = emptyList<IntRange>()
    var phoneOffsets = emptyList<IntRange>()
    var emailOffsets = emptyList<IntRange>()

    if (autoLinkType.contains(AutoLinkType.ALL)) {
        web = true
        phone = true
        email = true
    } else if (autoLinkType.contains(AutoLinkType.WEB)) {
        web = true
        webOffsets = extractAllURLOffsets(text)
    } else if (autoLinkType.contains(AutoLinkType.PHONE)) {
        phone = true
        phoneOffsets = extractAllPhoneNumberOffsets(text)
    } else if (autoLinkType.contains(AutoLinkType.EMAIL)) {
        email = true
        emailOffsets = extractAllEmailOffsets(text)
    }

    val annotatedString = buildAnnotatedString {
        append(text)
        if (web) {
            for (offset in webOffsets) {
                val urlTag = "${ANNOTATION_URL_PREFIX}${System.currentTimeMillis()}"
                addStringAnnotation(
                    tag = urlTag,
                    annotation = text.substring(offset.first, offset.last),
                    start = offset.first,
                    end = offset.last
                )
                addStyle(
                    SpanStyle(
                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                        textDecoration = TextDecoration.Underline
                    ),
                    offset.first,
                    offset.last
                )
            }
        }

        if (phone) {
            for (offset in phoneOffsets) {
                val phoneNumberTag =
                    "${ANNOTATION_PHONE_NUMBER_PREFIX}${System.currentTimeMillis()}"
                addStringAnnotation(
                    tag = phoneNumberTag,
                    annotation = text.substring(offset.first, offset.last),
                    start = offset.first,
                    end = offset.last
                )
                addStyle(
                    SpanStyle(
                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                        textDecoration = TextDecoration.Underline
                    ),
                    offset.first,
                    offset.last
                )
            }
        }

        if (email) {
            for (offset in emailOffsets) {
                val emailTag = "${ANNOTATION_EMAIL_PREFIX}${System.currentTimeMillis()}"
                addStringAnnotation(
                    tag = emailTag,
                    annotation = text.substring(offset.first, offset.last),
                    start = offset.first,
                    end = offset.last
                )
                addStyle(
                    SpanStyle(
                        color = MaterialTheme.koreatechColorPalette.hyperLink,
                        textDecoration = TextDecoration.Underline
                    ),
                    offset.first,
                    offset.last
                )
            }
        }
    }

    CustomClickableText(
        modifier = modifier,
        text = annotatedString
    ) { offset ->
        annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let { url ->
            if (url.tag.startsWith(ANNOTATION_URL_PREFIX)) {
                openWeb?.invoke(url.item)
            } else if (url.tag.startsWith(ANNOTATION_PHONE_NUMBER_PREFIX)) {
                openPhone?.invoke(url.item)
            } else if (url.tag.startsWith(ANNOTATION_EMAIL_PREFIX)) {
                openEmail?.invoke(url.item)
            }
        }
    }
}

sealed class AutoLinkType {
    data object WEB : AutoLinkType()
    data object PHONE : AutoLinkType()
    data object EMAIL : AutoLinkType()
    data object ALL : AutoLinkType()
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

const val ANNOTATION_URL_PREFIX = "url_"
const val ANNOTATION_PHONE_NUMBER_PREFIX = "phone_number_"
const val ANNOTATION_EMAIL_PREFIX = "email_"
