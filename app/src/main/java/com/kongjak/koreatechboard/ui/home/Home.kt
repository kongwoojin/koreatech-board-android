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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.ui.network.NetworkViewModel
import com.kongjak.koreatechboard.ui.settings.deptList
import com.kongjak.koreatechboard.util.routes.Department
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.util.UUID

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    networkViewModel: NetworkViewModel = hiltViewModel(),
    onArticleClick: (UUID, String) -> Unit
) {
    val networkState by networkViewModel.collectAsState()
    networkViewModel.collectSideEffect {
        networkViewModel.handleSideEffect(it)
    }
    homeViewModel.collectSideEffect {
        homeViewModel.handleSideEffect(it)
    }
    val isNetworkConnected = networkState.isConnected
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
            Text(text = stringResource(id = R.string.network_unavailable))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoardWidget(
    department: Department,
    onArticleClick: (UUID, String) -> Unit
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
                Text(text = stringResource(id = department.stringResource), fontSize = 16.sp)
            }

            ScrollableTabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                edgePadding = 0.dp
            ) {
                department.boards.forEachIndexed { index, board ->
                    Tab(
                        text = { Text(text = stringResource(id = board.stringResource)) },
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

@Composable
fun ArticleList(
    department: Department,
    page: Int,
    onArticleClick: (UUID, String) -> Unit
) {
    val key by remember { mutableStateOf(department.boards[page].board) }

    val homeBoardViewModel: HomeBoardViewModel = hiltViewModel(key = department.name)

    val context = LocalContext.current

    homeBoardViewModel.collectSideEffect {
        homeBoardViewModel.handleSideEffect(it) { stringId ->
            context.getString(stringId)
        }
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
                    Text(text = stringResource(R.string.error_no_article))
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
                Text(text = stringResource(R.string.error_server_down, statusCode))
                Button(onClick = {
                    coroutineScope.launch {
                        homeBoardViewModel.getApi(department.name, key)
                    }
                }) {
                    Text(text = stringResource(id = R.string.error_retry))
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
                    Text(text = stringResource(id = R.string.error_retry))
                }
            }
        }
    }
}
