package com.kongjak.koreatechboard.ui.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.components.text.AutoLinkText
import com.kongjak.koreatechboard.ui.components.text.AutoLinkType
import com.kongjak.koreatechboard.util.openUrl
import koreatech_board.app.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LicenseScreen() {
    val uriHandler = LocalUriHandler.current

    var bytes by remember {
        mutableStateOf(ByteArray(0))
    }

    LaunchedEffect(Unit) {
        bytes = Res.readBytes("files/licenses.txt")
    }

    AutoLinkText(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        text = bytes.decodeToString(),
        autoLinkType = arrayOf(AutoLinkType.WEB),
        openWeb = { url ->
            openUrl(uriHandler, url)
        }
    )
}
