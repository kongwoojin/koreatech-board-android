package com.kongjak.koreatechboard.ui.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kongjak.koreatechboard.constant.REGEX_BASE_URL
import com.kongjak.koreatechboard.domain.model.Article
import com.kongjak.koreatechboard.ui.components.HtmlView
import com.kongjak.koreatechboard.ui.components.WebView
import com.kongjak.koreatechboard.ui.components.dialog.ImageDialog
import com.kongjak.koreatechboard.ui.components.text.FileText
import com.kongjak.koreatechboard.ui.theme.articleSubText
import com.kongjak.koreatechboard.ui.theme.articleTitle
import com.kongjak.koreatechboard.util.rememberHtmlState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class, ExperimentalUuidApi::class)
@Composable
fun ArticleScreen(
    articleViewModel: ArticleViewModel = koinViewModel(),
    uuid: Uuid,
    department: String,
    isDarkTheme: Boolean,
    setExternalLink: (String) -> Unit
) {
    articleViewModel.collectSideEffect {
        articleViewModel.handleSideEffect(it)
    }
    val uiState by articleViewModel.collectAsState()

    val isLoading = uiState.isLoading

    val pullToRefreshState = rememberPullToRefreshState()

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            articleViewModel.getArticleData(department, uuid)
        }
    }

    LaunchedEffect(key1 = isLoading) {
        if (isLoading) return@LaunchedEffect
        pullToRefreshState.endRefresh()
    }

    val data = uiState.article

    LaunchedEffect(key1 = Unit) {
        pullToRefreshState.startRefresh()
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        with(uiState) {
            when {
                isLoaded && isSuccess -> {
                    ArticleView(
                        data = data,
                        isDarkTheme = isDarkTheme,
                        setExternalLink = setExternalLink
                    )
                }

                isLoaded && !isSuccess -> {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.error)
                    }
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
            indicator = { pullRefreshState ->
                PullToRefreshDefaults.Indicator(
                    state = pullRefreshState,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

@Composable
fun ArticleView(
    data: Article?,
    isDarkTheme: Boolean,
    setExternalLink: (articleUrl: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        data?.let {
            LaunchedEffect(key1 = Unit) {
                setExternalLink(it.articleUrl)
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.navigationBars)
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

                var baseUrl = Regex(REGEX_BASE_URL).find(it.articleUrl)?.value
                    ?: "https://www.koreatech.ac.kr"

                /*
                     If baseUrl is https://koreatech.ac.kr, replace it with https://www.koreatech.ac.kr
                     Because, if baseUrl is https://koreatech.ac.kr, okhttp will throw CLEARTEXT communication error
                     */
                if (baseUrl.contains("https://koreatech.ac.kr")) {
                    baseUrl = "https://www.koreatech.ac.kr"
                }

                var showImageDialog by remember { mutableStateOf(false) }
                var imageUri by remember { mutableStateOf("") }

                val htmlState = rememberHtmlState(
                    baseUrl = baseUrl,
                    html = it.content,
                    isDarkTheme = isDarkTheme
                )

                HtmlView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    htmlState = htmlState,
                    image = { url, description ->
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    imageUri = url
                                    showImageDialog = true
                                },
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                            contentDescription = description,
                            contentScale = ContentScale.FillWidth,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        )
                    },
                    webView = { html ->
                        WebView(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            html = html,
                            baseUrl = baseUrl,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        )
                    }
                )

                if (showImageDialog) {
                    ImageDialog(
                        imageUrl = imageUri,
                        onDismissRequest = { showImageDialog = false }
                    )
                }

                FileText(
                    modifier = Modifier.padding(16.dp),
                    files = it.files
                )
            }
        }
    }
}
