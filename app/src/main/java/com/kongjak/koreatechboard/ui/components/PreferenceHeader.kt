package com.kongjak.koreatechboard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = MaterialTheme.typography.labelMedium.fontSize
        )
    }
}

@Preview
@Composable
fun PreferenceHeaderPreview() {
    PreferenceHeader(title = "Settings")
}