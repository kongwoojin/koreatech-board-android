package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    BasicPreference(modifier = modifier, title = title, onClick = { onClick() })
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    BasicPreference(modifier = modifier, title = title, summary = summary, onClick = { onClick() })
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    BasicPreference(modifier = modifier, icon = icon, title = title, onClick = { onClick() })
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    summary: String,
    onClick: () -> Unit
) {
    BasicPreference(modifier = modifier, icon = icon, title = title, summary = summary, onClick = { onClick() })
}

@Preview
@Composable
fun PreferencePreview() {
    Column {
        Preference(title = "Title") {}
        Preference(title = "Title", summary = "summary") {}
        Preference(icon = Icons.Filled.Settings, title = "Title") {}
        Preference(icon = Icons.Filled.Settings, title = "Title", summary = "summary") {}
    }
}
