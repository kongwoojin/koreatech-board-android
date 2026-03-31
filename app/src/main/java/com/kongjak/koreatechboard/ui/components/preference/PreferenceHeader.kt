package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Deprecated("Use PreferenceColumn instead", ReplaceWith("PreferenceColumn"))
@Composable
fun PreferenceHeader(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp),
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
            fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
            color = MaterialTheme.typography.labelLarge.color
        )
    }
}

@Preview
@Composable
fun PreferenceHeaderPreview() {
    PreferenceHeader(title = "Settings")
}
