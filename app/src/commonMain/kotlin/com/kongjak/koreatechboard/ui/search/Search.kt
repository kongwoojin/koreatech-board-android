package com.kongjak.koreatechboard.ui.search

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kongjak.koreatechboard.ui.board.BoardError
import com.kongjak.koreatechboard.ui.components.BoardItem
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.search_no_result
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
@Composable
fun SearchScreen(
    department: String,
    board: String,
    title: String,
    onArticleClick: (Uuid, String) -> Unit,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    searchViewModel.collectSideEffect {
        searchViewModel.handleSideEffect(it)
    }

    val uiState by searchViewModel.collectAsState()

    LaunchedEffect(key1 = Unit) {
        searchViewModel.getAPI(department, board, title)
    }

    val lazyPostList = uiState.boardData.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(lazyPostList.itemCount) { index ->
            val boardItem = lazyPostList[index]
            boardItem?.let {
                BoardItem(
                    modifier = Modifier
                        .selectable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            selected = false,
                            onClick = {
                                onArticleClick(it.uuid, department)
                            }
                        ),
                    title = it.title,
                    writer = it.writer,
                    isNew = it.isNew,
                    date = it.writeDate
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.Gray
                )
            }
        }

        item {
            Spacer(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }

        lazyPostList.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }

                loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }

                loadState.refresh is LoadState.Error -> {
                    val errorMessage =
                        (loadState.refresh as LoadState.Error).error.localizedMessage
                    item { BoardError(errorMessage ?: "") }
                }

                loadState.append is LoadState.Error -> {
                    val errorMessage =
                        (loadState.append as LoadState.Error).error.localizedMessage
                    item { BoardError(errorMessage ?: "") }
                }

                loadState.refresh is LoadState.NotLoading -> {
                    if (lazyPostList.itemCount == 0) {
                        item { Text(text = stringResource(Res.string.search_no_result)) }
                    }
                }
            }
        }
    }
}
