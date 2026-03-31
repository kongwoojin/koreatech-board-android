package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.theme.preferenceColumnTitle

@Composable
fun PreferenceColumn(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun PreferenceColumn(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            text = title,
            style = MaterialTheme.typography.preferenceColumnTitle
        )
        content()
    }
}

@Preview
@Composable
fun PreferenceColumnPreview() {
    Surface {
        PreferenceColumn() {
            Preference(title = "Title") {
            }
            Preference(title = "Title") {
            }
        }
    }
}
