package com.kongjak.koreatechboard.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kongjak.koreatechboard.ui.settings.deptList
import com.kongjak.koreatechboard.util.routes.Department
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.error_no_article
import koreatech_board.app.generated.resources.error_retry
import koreatech_board.app.generated.resources.error_server_down
import koreatech_board.app.generated.resources.network_unavailable
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    onArticleClick: (Uuid, String) -> Unit
) {
    homeViewModel.collectSideEffect {
        homeViewModel.handleSideEffect(it)
    }
    val isNetworkConnected = true
    if (isNetworkConnected) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            val homeUiState by homeViewModel.collectAsState()
            val selectedDepartmentIndex = homeUiState.department
            BoardWidget(department = Department.School, onArticleClick = onArticleClick)
            BoardWidget(department = Department.Dorm, onArticleClick = onArticleClick)

            val selectedDepartment = deptList[selectedDepartmentIndex]
            BoardWidget(department = selectedDepartment, onArticleClick = onArticleClick)
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(Res.string.network_unavailable))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalUuidApi::class)
@Composable
fun BoardWidget(
    department: Department,
    onArticleClick: (Uuid, String) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        department.boards.size
    }

    val coroutineScope = rememberCoroutineScope()

    val tabIndex = pagerState.currentPage

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = stringResource(department.stringResource), fontSize = 16.sp)
            }

            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                edgePadding = 0.dp
            ) {
                department.boards.forEachIndexed { index, board ->
                    Tab(
                        text = { Text(text = stringResource(board.stringResource)) },
                        selected = tabIndex == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                ArticleList(department, page, onArticleClick)
            }
        }
    }
}

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
@Composable
fun ArticleList(
    department: Department,
    page: Int,
    onArticleClick: (Uuid, String) -> Unit
) {
    val key by remember { mutableStateOf(department.boards[page].board) }

    val homeBoardViewModel = koinViewModel<HomeBoardViewModel>(key = department.name)

    homeBoardViewModel.collectSideEffect {
        homeBoardViewModel.handleSideEffect(it)
    }

    LaunchedEffect(key1 = department.name, key2 = key) {
        homeBoardViewModel.getApi(department.name, key)
    }

    val uiState by homeBoardViewModel.collectAsState()
    val isSuccess = uiState.boardData[key]?.isSuccess ?: false
    val isLoaded = uiState.boardData[key]?.isLoaded ?: false
    val boardData = uiState.boardData[key]?.boardData ?: emptyList()
    val statusCode = uiState.boardData[key]?.statusCode ?: 0

    val coroutineScope = rememberCoroutineScope()

    if (!isLoaded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (isSuccess && statusCode == 200) {
            if (boardData.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(Res.string.error_no_article))
                }
            } else {
                Column {
                    boardData.forEach { data ->
                        Box(
                            modifier = Modifier
                                .clickable {
                                    onArticleClick(data.uuid, department.name)
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                text = data.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.Gray)
                    }
                }
            }
        } else if (isSuccess && statusCode != 200) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = stringResource(Res.string.error_server_down, statusCode))
                Button(onClick = {
                    coroutineScope.launch {
                        homeBoardViewModel.getApi(department.name, key)
                    }
                }) {
                    Text(text = stringResource(Res.string.error_retry))
                }
            }
        } else if (!isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = uiState.boardData[key]?.error ?: "")
                Button(onClick = {
                    coroutineScope.launch {
                        homeBoardViewModel.getApi(department.name, key)
                    }
                }) {
                    Text(text = stringResource(Res.string.error_retry))
                }
            }
        }
    }
}
