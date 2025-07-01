package com.kongjak.koreatechboard.ui.notice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.components.BoardItem
import com.kongjak.koreatechboard.ui.settings.deptList
import com.kongjak.koreatechboard.util.routes.BoardItem
import com.kongjak.koreatechboard.util.routes.Department
import koreatech_board.app.generated.resources.Res
import koreatech_board.app.generated.resources.content_description_delete
import koreatech_board.app.generated.resources.department_and_board
import koreatech_board.app.generated.resources.no_new_notice
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
@ExperimentalMaterial3Api
@Composable
fun Notice(
    modifier: Modifier = Modifier,
    onArticleClick: (Uuid, String) -> Unit,
    noticeViewModel: NoticeViewModel = koinViewModel()
) {
    noticeViewModel.collectSideEffect {
        noticeViewModel.handleSideEffect(it)
    }

    val uiState by noticeViewModel.collectAsState()

    val checkedList = uiState.selectedDepartment
    val userDepartment = uiState.userDepartment

    LaunchedEffect(key1 = checkedList) {
        noticeViewModel.getAllNotices()
    }

    if (!uiState.isLoaded) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val options = listOf(
                        stringResource(Department.School.stringResource),
                        stringResource(Department.Dorm.stringResource),
                        stringResource(deptList[userDepartment].stringResource)
                    )

                    MultiChoiceSegmentedButtonRow {
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ),
                                onCheckedChange = {
                                    if (index in checkedList) {
                                        noticeViewModel.removeSelectedDepartment(index)
                                    } else {
                                        noticeViewModel.addSelectedDepartment(index)
                                    }
                                },
                                checked = index in checkedList
                            ) {
                                Text(
                                    text = label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.articles.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(Res.string.no_new_notice)
                        )
                    }
                }
            } else {
                items(
                    items = uiState.articles,
                    key = { it.uuid }
                ) { article ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                noticeViewModel.deleteNotice(article.uuid)
                            }
                            true
                        }
                    )
                    AnimatedVisibility(
                        visible = true,
                        exit = fadeOut(spring())
                    ) {
                        SwipeToDismissBox(
                            state = dismissState,
                            modifier = Modifier,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = Dp(20f)),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = stringResource(Res.string.content_description_delete)
                                        )
                                    }
                                }
                            }
                        ) {
                            BoardItem(
                                modifier = Modifier
                                    .selectable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        selected = false,
                                        onClick = {
                                            onArticleClick(article.uuid, article.department)
                                            noticeViewModel.updateRead(article.uuid, true)
                                        }
                                    ),
                                aboveText = stringResource(
                                    Res.string.department_and_board,
                                    stringResource(Department.valueOf(article.department).stringResource),
                                    stringResource(BoardItem.valueOf(article.board).stringResource)
                                ),
                                title = article.title,
                                writer = article.writer,
                                date = article.date,
                                isRead = article.read
                            )
                        }
                    }
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
        }
    }
}
