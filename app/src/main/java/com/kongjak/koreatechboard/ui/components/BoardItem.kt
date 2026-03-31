package com.kongjak.koreatechboard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.R
import com.kongjak.koreatechboard.ui.theme.boardItemAboveText
import com.kongjak.koreatechboard.ui.theme.boardItemSubText
import com.kongjak.koreatechboard.ui.theme.boardItemTitle

@Composable
fun BoardItem(
    modifier: Modifier = Modifier,
    aboveText: String? = null,
    title: String,
    writer: String,
    date: String,
    isNew: Boolean = false,
    isRead: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (aboveText != null) {
            Text(
                text = aboveText,
                style = if (isRead) {
                    MaterialTheme.typography.boardItemAboveText.copy(
                        fontStyle = FontStyle.Italic,
                        color = Color.Gray
                    )
                } else {
                    MaterialTheme.typography.boardItemAboveText
                }
            )
        }
        Column {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(),
                style = when {
                    isNew -> MaterialTheme.typography.boardItemTitle.copy(
                        fontWeight = FontWeight.Bold
                    )

                    isRead -> MaterialTheme.typography.boardItemTitle.copy(
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )

                    else -> MaterialTheme.typography.boardItemTitle
                }
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = writer,
                    style = if (isRead) {
                        MaterialTheme.typography.boardItemSubText.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    } else {
                        MaterialTheme.typography.boardItemSubText
                    }
                )
                Text(
                    text = date,
                    style = if (isRead) {
                        MaterialTheme.typography.boardItemSubText.copy(
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray
                        )
                    } else {
                        MaterialTheme.typography.boardItemSubText
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BoardItemPreview() {
    Surface {
        Column {
            BoardItem(
                aboveText = stringResource(id = R.string.department_cse),
                title = "2021학년도 2학기 학부생 교과목평가 실시 안내",
                writer = "학사팀",
                date = "2021.09.01",
                isNew = true
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.Gray
            )
            BoardItem(
                aboveText = stringResource(id = R.string.department_cse),
                title = "2021학년도 2학기 학부생 교과목평가 실시 안내",
                writer = "학사팀",
                date = "2021.09.01",
                isNew = false,
                isRead = true
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = Color.Gray
            )
            BoardItem(
                title = "2021학년도 2학기 학부생 교과목평가 실시 안내",
                writer = "학사팀",
                date = "2021.09.01",
                isNew = false
            )
        }
    }
}
