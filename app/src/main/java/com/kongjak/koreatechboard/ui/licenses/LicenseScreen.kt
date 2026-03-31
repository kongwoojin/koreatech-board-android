package com.kongjak.koreatechboard.ui.licenses

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.components.text.AutoLinkText
import com.kongjak.koreatechboard.ui.components.text.AutoLinkType

@Composable
fun LicenseScreen() {
    val context = LocalContext.current

    context.assets.open("open_source_licenses.txt").use {
        val bytes = it.readBytes()
        val text = bytes.decodeToString()

        AutoLinkText(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.navigationBars),
            text = text,
            autoLinkType = arrayOf(AutoLinkType.WEB),
            openWeb = { url ->
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(url))
            }
        )
    }
}
