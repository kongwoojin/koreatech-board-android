package com.kongjak.koreatechboard.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Locale

@Composable
fun WebView(
    modifier: Modifier = Modifier,
    html: String,
    baseUrl: String,
    loading: @Composable () -> Unit = {}
) {
    var isWebViewLoaded by remember { mutableStateOf(false) }

    if (!isWebViewLoaded) {
        loading()
    }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    isForceDarkAllowed = true
                }
                webViewClient = KoreatechBoardWebViewClient(
                    setLoaded = { loaded ->
                        isWebViewLoaded = loaded
                    }
                )
                isVerticalScrollBarEnabled = false

                setBackgroundColor(Color.TRANSPARENT)
                settings.apply {
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setSupportZoom(false)
                    builtInZoomControls = false
                }
            }
        },
        modifier = modifier,
        update = {
            val width = getScreenWidth(it.context)
            it.loadDataWithBaseURL(
                baseUrl,
                fullHtml(width, html),
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}

private fun getScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = wm.currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

private fun fullHtml(width: Int, html: String): String {
    return """
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body>
        <style>
            body {margin: 0!important;padding: 0!important;}
            img {display: inline;height: auto;max-width: 100%;width: $width;}
            table {height: auto;max-width: 100%;width: auto!important;}
            h1, h2, h3, h4, h5, h6, p, span {font-size: 16px!important;}
            *{line-height: normal!important;}
        </style>
        $html
        </body>
        </html>
    """.trimIndent()
}

class KoreatechBoardWebViewClient(private val setLoaded: (Boolean) -> Unit = {}) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(view!!.context, Uri.parse(url))
            return true
        }
        return false
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if (request?.url == null || request.url.toString().startsWith("data:")) {
            return super.shouldInterceptRequest(view, request)
        }

        val imageRequest = ImageRequest.Builder(view!!.context)
            .data(request.url)
            .build()

        val drawable = runBlocking {
            view.context.imageLoader.execute(imageRequest).drawable
        }

        return when {
            request.url.toString().lowercase(Locale.ROOT).contains(".jpg") ||
                request.url.toString().lowercase(Locale.ROOT).contains(".jpeg") -> {
                return WebResourceResponse(
                    "image/jpg",
                    "UTF-8",
                    getBitmapInputStream(drawable!!.toBitmap(), CompressFormat.JPEG)
                )
            }

            request.url.toString().lowercase(Locale.ROOT).contains(".png") -> {
                return WebResourceResponse(
                    "image/png",
                    "UTF-8",
                    getBitmapInputStream(drawable!!.toBitmap(), CompressFormat.PNG)
                )
            }

            request.url.toString().lowercase(Locale.ROOT).contains(".webp") -> {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    WebResourceResponse(
                        "image/webp",
                        "UTF-8",
                        getBitmapInputStream(drawable!!.toBitmap(), CompressFormat.WEBP_LOSSY)
                    )
                } else {
                    WebResourceResponse(
                        "image/webp",
                        "UTF-8",
                        getBitmapInputStream(drawable!!.toBitmap(), CompressFormat.WEBP)
                    )
                }
            }

            else -> super.shouldInterceptRequest(view, request)
        }
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        setLoaded(false)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        setLoaded(true)
    }
}

private fun getBitmapInputStream(bitmap: Bitmap, compressFormat: CompressFormat): InputStream {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(compressFormat, 80, byteArrayOutputStream)
    val bitmapData: ByteArray = byteArrayOutputStream.toByteArray()
    return ByteArrayInputStream(bitmapData)
}
