package com.kongjak.koreatechboard.ui.article

import android.widget.TextView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kongjak.koreatechboard.ui.components.HtmlView
import com.kongjak.koreatechboard.ui.theme.articleSubText
import com.kongjak.koreatechboard.ui.theme.articleTitle
import com.kongjak.koreatechboard.ui.viewmodel.ThemeViewModel
import com.kongjak.koreatechboard.util.fileText
import java.util.UUID

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleScreen(
    articleViewModel: ArticleViewModel,
    themeViewModel: ThemeViewModel,
    department: String,
    uuid: UUID
) {
    val uiState by articleViewModel.uiState.collectAsState()

    val isLoading = uiState.isLoading

    val pullRefreshState =
        rememberPullRefreshState(isLoading, { articleViewModel.getArticleData(department, uuid) })

    val data = uiState.article
    val context = LocalContext.current
    val contentTextView = remember { TextView(context) }
    val filesTextView = remember { TextView(context) }

    LaunchedEffect(key1 = Unit) {
        articleViewModel.getArticleData(department, uuid)
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            data?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.articleTitle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = it.writer,
                                style = MaterialTheme.typography.articleSubText
                            )
                            Text(
                                text = it.date,
                                style = MaterialTheme.typography.articleSubText
                            )
                        }
                    }

                    HtmlView(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize(),
                        html = data?.content!!,
                        themeViewModel.isDarkTheme.value ?: isSystemInDarkTheme(),
                        image = { url, description ->
                            GlideImage(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxSize(),
                                model = url,
                                contentDescription = description
                            )
                        }
                    )

                    key(themeViewModel.isDarkTheme) {
                        var isDarkTheme = themeViewModel.isDarkTheme.value

                        if (isDarkTheme == null) isDarkTheme = isSystemInDarkTheme()

                        val textColor =
                            if (isDarkTheme == true) {
                                Color(0xFFFFFFFF)
                            } else {
                                Color(0xFF000000)
                            }

                        AndroidView(
                            factory = { filesTextView },
                            modifier = Modifier
                                .padding(16.dp),
                            update = {
                                it.fileText = data.files
                                it.setTextColor(textColor.toArgb())
                            }
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}
